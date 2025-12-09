package br.edu.ufape.alugafacil.models;

import br.edu.ufape.alugafacil.dto.SubscriptionDto;
import br.edu.ufape.alugafacil.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String subscriptionId;

    private LocalDate startDate;
    private LocalDate nextBillingDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    public static Subscription getEntity(SubscriptionDto subscriptionDto) {
        Subscription subscription = Subscription.builder()
                .subscriptionId(subscriptionDto.getSubscriptionId() != null && !subscriptionDto.getSubscriptionId().isEmpty() ? subscriptionDto.getSubscriptionId() : null)
                .startDate(subscriptionDto.getStartDate())
                .nextBillingDate(subscriptionDto.getNextBillingDate())
                .status(subscriptionDto.getStatus())
                .plan(Plan.getEntity(subscriptionDto.getPlanDto()))
                .build();
        return subscription;
    }
}
