package br.com.db.voting.controllers;

import br.com.db.voting.exceptions.GlobalExceptionHandler;
import br.com.db.voting.models.Session;
import br.com.db.voting.records.CreateSessionRequest;
import br.com.db.voting.services.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SessionControllerTest {

    private Long topicId = 1L;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldCreateSessionSuccessfully() throws Exception {
        LocalDateTime finalDatetime = LocalDateTime.now().plusMinutes(30);
        CreateSessionRequest request = new CreateSessionRequest(topicId, finalDatetime);

        Session mockSession = new Session();
        mockSession.setId(1L);
        mockSession.setTopic(null);
        mockSession.setFinalDateTime(finalDatetime);

        when(sessionService.createSession(topicId, finalDatetime)).thenReturn(mockSession);

        String jsonContent = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk());

        verify(sessionService).createSession(topicId, finalDatetime);
    }

    @Test
    public void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
        CreateSessionRequest invalidRequest = new CreateSessionRequest(null, null);

        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenFinalDatetimeIsInPast() throws Exception {
        LocalDateTime pastDatetime = LocalDateTime.now().minusDays(1);
        CreateSessionRequest request = new CreateSessionRequest(topicId, pastDatetime);

        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}