package br.com.db.voting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.db.voting.models.Vote;
import br.com.db.voting.records.CreateVoteRequest;
import br.com.db.voting.services.VotingService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/voting")
public class VotingController {
	
    @Autowired
    private VotingService votingService;

    @PostMapping("/{topicId}")
    public ResponseEntity<Vote> vote(@PathVariable Long topicId, @Valid @RequestBody CreateVoteRequest voteRequest) {
        Vote registeredVote = votingService.vote(voteRequest.vote(), topicId, voteRequest.associatedId());
        return ResponseEntity.ok(registeredVote);
    }

}
