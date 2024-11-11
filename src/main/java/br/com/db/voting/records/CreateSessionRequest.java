package br.com.db.voting.records;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateSessionRequest(@NotNull Long topic,  @FutureOrPresent LocalDateTime finalDatetime) {}

