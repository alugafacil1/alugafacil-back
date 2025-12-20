package br.edu.ufape.alugafacil.dtos.subscription;

import br.edu.ufape.alugafacil.enums.PaymentStatus;
import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponseDTO(
    UUID subscriptionId,
    UUID planId,
    String planName,
    Integer priceInCents,
    LocalDate startDate,
    LocalDate nextBillingDate,
    PaymentStatus status
) {}