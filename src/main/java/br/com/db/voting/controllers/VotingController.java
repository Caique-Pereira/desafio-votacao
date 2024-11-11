package br.com.db.voting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.db.voting.models.Vote;
import br.com.db.voting.services.VotingService;


@RestController
@RequestMapping("/voting")
public class VotingController {
	
    @Autowired
    private VotingService votingService;

    @PostMapping("/{topicId}")
    public Vote vote(@RequestParam boolean vote,@PathVariable Long topicId , @RequestParam String cpf, @RequestParam Long associatedId) {
        return votingService.vote(vote, topicId, cpf,associatedId );
    } 

}
