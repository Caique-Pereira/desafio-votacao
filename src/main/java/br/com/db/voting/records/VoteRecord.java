package br.com.db.voting.records;

public record VoteRecord(Long id, TopicRecord topic, Boolean vote, String cpf) {}
