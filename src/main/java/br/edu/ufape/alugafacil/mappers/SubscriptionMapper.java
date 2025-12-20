package br.edu.ufape.alugafacil.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionResponseDTO;
import br.edu.ufape.alugafacil.models.Subscription;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "subscriptionId", target = "subscriptionId")

    @Mapping(source = "plan.planId", target = "planId")
    @Mapping(source = "plan.name", target = "planName")
    @Mapping(source = "plan.priceInCents", target = "priceInCents")

    // Mapeia datas e status
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "nextBillingDate", target = "nextBillingDate")
    @Mapping(source = "status", target = "status")
    
    SubscriptionResponseDTO toResponse(Subscription subscription);
}