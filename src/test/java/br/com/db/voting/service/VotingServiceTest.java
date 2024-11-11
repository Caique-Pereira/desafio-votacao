package br.com.db.voting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import br.com.db.voting.models.Topic;
import br.com.db.voting.models.Vote;
import br.com.db.voting.records.CreateVoteRequest;
import br.com.db.voting.repositories.SessionRepository;
import br.com.db.voting.repositories.TopicRepository;
import br.com.db.voting.repositories.VoteRepository;
import br.com.db.voting.services.VotingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VotingServiceTest {

    private final Long topicId = 1L;
    private final Long associatedId = 1L;
    private final Boolean voteValue = true;

    @InjectMocks
    private VotingService votingService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private VoteRepository voteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldVoteSuccessfullyWhenAllConditionsAreMet() {
        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        CreateVoteRequest voteRequest = new CreateVoteRequest(voteValue, associatedId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic));
        when(sessionRepository.existsByTopicAndFinalDateTimeAfter(eq(mockTopic), any(LocalDateTime.class)))
                .thenReturn(true);
        when(voteRepository.existsByAssociatedIdAndTopic(eq(voteRequest.associatedId()), eq(mockTopic)))
                .thenReturn(false);

        Vote result = votingService.vote(voteRequest.vote(), topicId, voteRequest.associatedId());

        assertNotNull(result);
        assertEquals(mockTopic, result.getTopic());
        assertEquals(voteRequest.vote(), result.isVote());
        assertEquals(voteRequest.associatedId(), result.getAssociatedId());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void shouldThrowExceptionWhenTopicNotFound() {
    	CreateVoteRequest voteRequest = new CreateVoteRequest(voteValue, associatedId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votingService.vote(voteRequest.vote(), topicId, voteRequest.associatedId());
        });

        assertEquals("Pauta com ID " + topicId + " não encontrada.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenVotingSessionIsClosed() {
        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        CreateVoteRequest voteRequest = new CreateVoteRequest(voteValue, associatedId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic));
        when(sessionRepository.existsByTopicAndFinalDateTimeAfter(eq(mockTopic), any(LocalDateTime.class)))
                .thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votingService.vote(voteRequest.vote(), topicId, voteRequest.associatedId());
        });

        assertEquals("A sessão de votos para a pauta está fechada.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenVoteAlreadyExistsForUserAndTopic() {
        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        CreateVoteRequest voteRequest = new CreateVoteRequest(voteValue, associatedId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic));
        when(sessionRepository.existsByTopicAndFinalDateTimeAfter(eq(mockTopic), any(LocalDateTime.class)))
                .thenReturn(true);
        when(voteRepository.existsByAssociatedIdAndTopic(eq(voteRequest.associatedId()), eq(mockTopic)))
                .thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votingService.vote(voteRequest.vote(), topicId, voteRequest.associatedId());
        });

        assertEquals("Essa Pauta já foi votada por esse usuário", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenVoteValueIsNull() {
        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        CreateVoteRequest voteRequest = new CreateVoteRequest(null, associatedId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic));
        when(sessionRepository.existsByTopicAndFinalDateTimeAfter(eq(mockTopic), any(LocalDateTime.class)))
                .thenReturn(true);
        when(voteRepository.existsByAssociatedIdAndTopic(eq(voteRequest.associatedId()), eq(mockTopic)))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votingService.vote(voteRequest.vote(), topicId, voteRequest.associatedId());
        });

        assertEquals("O voto (sim/não) deve ser fornecido.", exception.getMessage());
    }
}
