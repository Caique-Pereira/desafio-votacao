package br.com.db.voting.controllers;

import br.com.db.voting.exceptions.ConflictException;
import br.com.db.voting.exceptions.GlobalExceptionHandler;
import br.com.db.voting.models.Topic;
import br.com.db.voting.models.Vote;
import br.com.db.voting.services.VotingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VotingControllerTest {

    @Mock
    private VotingService votingService;

    @InjectMocks
    private VotingController votingController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(votingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void shouldReturnVoteSuccessfully() throws Exception {
        boolean voteValue = true;
        Long topicId = 1L;
        String cpf = "12345678901";
        Long associatedId = 2L;

        Topic mockTopic = new Topic();
        mockTopic.setId(topicId);

        Vote mockVote = new Vote();
        mockVote.setId(1L);
        mockVote.setTopic(mockTopic);
        mockVote.setVote(voteValue);
        mockVote.setAssociatedId(associatedId);

        when(votingService.vote(voteValue, topicId, cpf, associatedId)).thenReturn(mockVote);

        mockMvc.perform(post("/voting/{topicId}", topicId)
                        .param("vote", String.valueOf(voteValue))
                        .param("cpf", cpf)
                        .param("associatedId", String.valueOf(associatedId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockVote.getId()))
                .andExpect(jsonPath("$.topic.id").value(mockVote.getTopic().getId()))
                .andExpect(jsonPath("$.vote").value(mockVote.isVote()))
                .andExpect(jsonPath("$.associatedId").value(mockVote.getAssociatedId()));

        verify(votingService).vote(voteValue, topicId, cpf, associatedId);
    }

    @Test
    public void shouldReturnBadRequestWhenMissingRequiredParams() throws Exception {
        mockMvc.perform(post("/voting/{topicId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenVoteValueIsNull() throws Exception {
        Long topicId = 1L;
        String cpf = "12345678901";
        Long associatedId = 2L;

        mockMvc.perform(post("/voting/{topicId}", topicId)
                        .param("cpf", cpf)
                        .param("associatedId", String.valueOf(associatedId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnConflictWhenVoteAlreadyExists() throws Exception {
        boolean voteValue = true;
        Long topicId = 1L;
        String cpf = "12345678901";
        Long associatedId = 2L;

        when(votingService.vote(voteValue, topicId, cpf, associatedId))
                .thenThrow(new ConflictException("Essa Pauta já foi votada por esse usuário"));

        mockMvc.perform(post("/voting/{topicId}", topicId)
                        .param("vote", String.valueOf(voteValue))
                        .param("cpf", cpf)
                        .param("associatedId", String.valueOf(associatedId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Essa Pauta já foi votada por esse usuário"));
    }



    @Test
    public void shouldReturnInternalServerErrorWhenUnexpectedErrorOccurs() throws Exception {
        boolean voteValue = true;
        Long topicId = 1L;
        String cpf = "12345678901";
        Long associatedId = 2L;

        when(votingService.vote(voteValue, topicId, cpf, associatedId))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(post("/voting/{topicId}", topicId)
                        .param("vote", String.valueOf(voteValue))
                        .param("cpf", cpf)
                        .param("associatedId", String.valueOf(associatedId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro inesperado"));
    }
}