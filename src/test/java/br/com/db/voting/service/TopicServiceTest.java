package br.com.db.voting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import br.com.db.voting.services.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.db.voting.models.Topic;
import br.com.db.voting.repositories.TopicRepository;
import br.com.db.voting.repositories.VoteRepository;

public class TopicServiceTest {

    private Long topicId = 1L;

    @InjectMocks
    private TopicService topicService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private VoteRepository voteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateTopicWhenDescriptionIsValid() {
        Topic topic = new Topic();
        topic.setDescription("Valid Topic");

        when(topicRepository.save(topic)).thenReturn(topic);

        Topic createdTopic = topicService.createTopic(topic);

        assertNotNull(createdTopic);
        assertEquals("Valid Topic", createdTopic.getDescription());
        verify(topicRepository).save(topic);
    }

    @Test
    public void shouldThrowExceptionWhenTopicIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            topicService.createTopic(null);
        });

        assertEquals("Descrição não pode estar vazia", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIsBlank() {
        Topic topic = new Topic();
        topic.setDescription(" ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            topicService.createTopic(topic);
        });

        assertEquals("Descrição não pode estar vazia", exception.getMessage());
    }

    @Test
    public void shouldReturnVoteResultsWhenTopicExists() {
        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic));
        when(voteRepository.countPositiveVotesByTopic(mockTopic)).thenReturn(10L);
        when(voteRepository.countNegativeVotesByTopic(mockTopic)).thenReturn(5L);

        Map<String, Long> results = topicService.getVoteResults(topicId);

        assertNotNull(results);
        assertEquals(10L, results.get("Positives"));
        assertEquals(5L, results.get("Negative"));
    }

    @Test
    public void shouldThrowExceptionWhenTopicDoesNotExist() {
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            topicService.getVoteResults(topicId);
        });

        assertEquals("Tópico com ID " + topicId + " não encontrado.", exception.getMessage());
    }
}