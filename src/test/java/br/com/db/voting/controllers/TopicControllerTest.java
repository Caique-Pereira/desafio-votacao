package br.com.db.voting.controllers;

import br.com.db.voting.exceptions.GlobalExceptionHandler;
import br.com.db.voting.exceptions.TopicNotFoundException;
import br.com.db.voting.models.Topic;
import br.com.db.voting.services.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TopicControllerTest {

    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicController topicController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(topicController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void shouldCreateTopicSuccessfully() throws Exception {
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setDescription("Test Topic");

        when(topicService.createTopic(any(Topic.class))).thenReturn(topic);

        mockMvc.perform(post("/topics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Test Topic\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(topic.getId()))
                .andExpect(jsonPath("$.description").value(topic.getDescription()));

        verify(topicService).createTopic(any(Topic.class));
    }

    @Test
    public void shouldReturnBadRequestWhenCreatingTopicWithInvalidDescription() throws Exception {
        when(topicService.createTopic(any(Topic.class)))
                .thenThrow(new IllegalArgumentException("Descrição não pode estar vazia"));

        mockMvc.perform(post("/topics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Descrição não pode estar vazia"));
    }

    @Test
    public void shouldGetVoteResultsSuccessfully() throws Exception {
        Long topicId = 1L;
        Map<String, Long> mockResults = new HashMap<>();
        mockResults.put("Positives", 10L);
        mockResults.put("Negative", 5L);

        when(topicService.getVoteResults(topicId)).thenReturn(mockResults);

        mockMvc.perform(get("/topics/{topicId}/results", topicId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Positives").value(10))
                .andExpect(jsonPath("$.Negative").value(5));

        verify(topicService).getVoteResults(topicId);
    }

    @Test
    public void shouldReturnNotFoundWhenGettingResultsForNonexistentTopic() throws Exception {
        Long topicId = 999L;

        when(topicService.getVoteResults(topicId))
                .thenThrow(new TopicNotFoundException("Tópico com ID " + topicId + " não encontrado."));

        mockMvc.perform(get("/topics/{topicId}/results", topicId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tópico com ID " + topicId + " não encontrado."));
    }

}