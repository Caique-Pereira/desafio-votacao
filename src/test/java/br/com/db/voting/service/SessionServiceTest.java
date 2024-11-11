package br.com.db.voting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.db.voting.models.Session;
import br.com.db.voting.models.Topic;
import br.com.db.voting.repositories.SessionRepository;
import br.com.db.voting.repositories.TopicRepository;
import br.com.db.voting.services.SessionService;

public class SessionServiceTest {

    private Long topicId = 1L;

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private SessionRepository sessionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateSessionWhenAllConditionsAreMet() {
        LocalDateTime finalDatetime = LocalDateTime.now().plusHours(1);

        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic));
        when(sessionRepository.existsByTopicAndFinalDateTimeAfter(eq(mockTopic), any(LocalDateTime.class)))
                .thenReturn(false);

        Session createdSession = sessionService.createSession(topicId, finalDatetime);

        assertNotNull(createdSession);
        assertEquals(mockTopic, createdSession.getTopic());
        assertEquals(finalDatetime, createdSession.getFinalDateTime());
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    public void shouldThrowExceptionWhenTopicIdIsNull() {
        Long topicId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sessionService.createSession(topicId, LocalDateTime.now());
        });

        assertEquals("Id da pauta não pode ser nulo.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenTopicDoesNotExist() {
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sessionService.createSession(topicId, LocalDateTime.now());
        });

        assertEquals(String.format("Pauta com id %d não existe", topicId), exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenSessionAlreadyExistsForTopic() {
        LocalDateTime now = LocalDateTime.now();

        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic));
        when(sessionRepository.existsByTopicAndFinalDateTimeAfter(eq(mockTopic), any(LocalDateTime.class)))
                .thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sessionService.createSession(topicId, now.plusMinutes(10));
        });

        assertEquals("Já existe uma sessão aberta para esta pauta.", exception.getMessage());
    }

    @Test
    public void shouldUseDefaultFinalDatetimeWhenProvidedFinalDatetimeIsNull() {
        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(mockTopic));
        when(sessionRepository.existsByTopicAndFinalDateTimeAfter(eq(mockTopic), any(LocalDateTime.class)))
                .thenReturn(false);

        Session createdSession = sessionService.createSession(topicId, null);
        assertNotNull(createdSession);
        assertEquals(mockTopic, createdSession.getTopic());
        assertNotNull(createdSession.getFinalDateTime());
        assertTrue(createdSession.getFinalDateTime().isAfter(LocalDateTime.now()));
        verify(sessionRepository).save(any(Session.class));
    }
}