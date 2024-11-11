package br.com.db.voting.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.db.voting.models.Topic;
import br.com.db.voting.models.Vote;
import br.com.db.voting.repositories.SessionRepository;
import br.com.db.voting.repositories.TopicRepository;
import br.com.db.voting.repositories.VoteRepository;

@Service
public class VotingService {
	
	
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private VoteRepository voteRepository;
   
    
	  public Vote vote(Boolean voteValue, Long topicId, String cpf, Long associatedId) {
		    
		  Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Pauta com ID " + topicId + " não encontrada."));
		  if (!sessionRepository.existsByTopicAndFinalDateTimeAfter(topic, LocalDateTime.now())) throw new RuntimeException("A sessão de votos para a pauta está fechada.");
		  if (voteValue == null) throw new IllegalArgumentException("O voto (sim/não) deve ser fornecido.");  
		  if (voteRepository.existsByAssociatedIdAndTopic(associatedId, topic)) throw new RuntimeException("Essa Pauta já foi votada por esse usuário");
 
          Vote vote = new Vote();
          vote.setTopic(topic);
          vote.setVote(voteValue);
          vote.setAssociatedId(topicId);
          voteRepository.save(vote);
	        
	      return vote;
	    }
    
}
