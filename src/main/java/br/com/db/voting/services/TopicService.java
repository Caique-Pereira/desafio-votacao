package br.com.db.voting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.db.voting.models.Topic;
import br.com.db.voting.repositories.TopicRepository;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public Topic createTopic(Topic topic) {
        if (topic == null || topic.getDescription() == null || topic.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found with id: " + id));
    }

    public Topic updateTopic(Long id, Topic updatedTopic) {
        Topic existingTopic = getTopicById(id); 

        if (updatedTopic.getDescription() == null || updatedTopic.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }

        existingTopic.setDescription(updatedTopic.getDescription());
        return topicRepository.save(existingTopic);
    }

 
    public void deleteTopic(Long id) {
        Topic topic = getTopicById(id);
        topicRepository.delete(topic);
    }
}
