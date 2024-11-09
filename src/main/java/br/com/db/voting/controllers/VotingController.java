package br.com.db.voting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.db.voting.records.VoteRecord;
import br.com.db.voting.services.VotingService;




@RestController
@RequestMapping("/voting")
public class VotingController {
	
    @Autowired
    private VotingService votingService;
	
    @PostMapping()
    public VoteRecord votar( @RequestParam boolean _vote,@PathVariable Long _topicId , @RequestParam String _cpf) {
        return votingService.vote(_vote, _topicId, _cpf);
    }

}
