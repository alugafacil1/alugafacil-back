package br.edu.ufape.alugafacil.dtos.subscription;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record SubscriptionRequestDTO (
    @NotNull(message = "O ID do plano é obrigatório")
    UUID planId
){}
