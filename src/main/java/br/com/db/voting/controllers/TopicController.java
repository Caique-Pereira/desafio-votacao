package br.com.db.voting.controllers;

import br.com.db.voting.models.Topic;
import br.com.db.voting.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        Topic createdTopic = topicService.createTopic(topic);
        return ResponseEntity.ok(createdTopic);
    }
    
    @GetMapping("/{topicId}/results")
    public ResponseEntity<Map<String, Long>> getVoteResults(@PathVariable Long topicId) {
        Map<String, Long> results = topicService.getVoteResults(topicId);
        return ResponseEntity.ok(results);
    }
}
