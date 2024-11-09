package br.com.db.voting.records;

import java.time.LocalDateTime;

public record SessionRecord(Long id,TopicRecord topic, boolean isOpen, LocalDateTime initialDateTime, LocalDateTime finalDateTime ) {}
