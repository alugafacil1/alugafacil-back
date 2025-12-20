package br.edu.ufape.alugafacil.services.interfaces;

import java.util.UUID;

import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionRequestDTO;
import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionResponseDTO;

public interface ISubscriptionService {
    SubscriptionResponseDTO subscribe(UUID userId, SubscriptionRequestDTO request);
}
