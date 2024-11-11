package br.com.db.voting.records;

import jakarta.validation.constraints.NotNull;

public record CreateVoteRequest(@NotNull Boolean vote,@NotNull Long associatedId) {}
