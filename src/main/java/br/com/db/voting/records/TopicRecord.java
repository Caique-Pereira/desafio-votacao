package br.com.db.voting.records;

import br.com.db.voting.models.Topic;

public record TopicRecord(Long id , String description) {
	
	 public TopicRecord(Topic topic) {
	     this(topic.getId(), topic.getDescription());
	}
}
