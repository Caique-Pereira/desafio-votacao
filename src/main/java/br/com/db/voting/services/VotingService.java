package br.com.db.voting.services;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.db.voting.models.Topic;
import br.com.db.voting.models.Vote;
import br.com.db.voting.records.TopicRecord;
import br.com.db.voting.records.VoteRecord;
import br.com.db.voting.repositories.SessionRepository;
import br.com.db.voting.repositories.TopicRepository;
import br.com.db.voting.repositories.VoteRepository;
import br.com.db.voting.utilities.Utils;

@Service
public class VotingService {
	
	
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private VoteRepository voteRepository;
   
    
	  public VoteRecord vote(Boolean _vote, Long _topicId, String _cpf) {
		    
		    Topic topic = topicRepository.findById(_topicId).orElseThrow(() -> new RuntimeException("Pauta inválida"));
		    TopicRecord topicRecorder = new TopicRecord(topic);
		    sessionRepository.findOpenSessionByTopic(topic).orElseThrow(() -> new RuntimeException("A sessão de votos está fechada."));

	        
	        if (!Utils.isValidCpf(_cpf)) {
	            throw new RuntimeException("CPF inválido.");
	        }

	        if (voteRepository.existsByCpfAndTopic(_cpf, topic)) {
	            throw new RuntimeException("Essa Pauta já foi votado pelo CPF: " + _cpf);
	        }
		    
	        Vote vote = new Vote();
	        vote.setTopic(topic);
	        vote.setVote(_vote);
	        vote.setCpf(_cpf);
	        voteRepository.save(vote);
	        
	        
	        return new VoteRecord(vote.getId(), topicRecorder, vote.isVote(), vote.getCpf());
	     
	    }
	  
	    public List<VoteRecord> getAllVotes() {
	        return voteRepository.findAll().stream()
	                .map(vote -> new VoteRecord(
	                        vote.getId(),
	                        new TopicRecord(vote.getTopic().getId(), vote.getTopic().getDescription()),
	                        vote.isVote(),
	                        vote.getCpf()
	                ))
	                .collect(Collectors.toList());
	    }


	    public VoteRecord getVoteById(Long voteId) {
	        Vote vote = voteRepository.findById(voteId)
	                .orElseThrow(() -> new RuntimeException("Voto não encontrado com o ID: " + voteId));

	        Topic topic = vote.getTopic();
	        TopicRecord topicRecord = new TopicRecord(topic.getId(), topic.getDescription());

	        return new VoteRecord(vote.getId(), topicRecord, vote.isVote(), vote.getCpf());
	    }
	  

    
}
