package br.com.db.voting.services;

import java.util.HashMap;
import java.util.Map;

import br.com.db.voting.exceptions.TopicNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.db.voting.models.Topic;
import br.com.db.voting.repositories.TopicRepository;
import br.com.db.voting.repositories.VoteRepository;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private VoteRepository voteRepository;

    public Topic createTopic(Topic topic) {
        if (topic == null || topic.getDescription() == null || topic.getDescription().isBlank()) {
            throw new IllegalArgumentException("Descrição não pode estar vazia");
        }
        return topicRepository.save(topic);
    }
    
    public Map<String, Long> getVoteResults(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Tópico com ID " + topicId + " não encontrado."));

        long positiveVotes = voteRepository.countPositiveVotesByTopic(topic);
        long negativeVotes = voteRepository.countNegativeVotesByTopic(topic);

        Map<String, Long> results = new HashMap<>();
        results.put("Positives", positiveVotes);
        results.put("Negative", negativeVotes);

        return results;
    }


}
