package br.edu.ufape.alugafacil.dto;

import br.edu.ufape.alugafacil.enums.PaymentStatus;
import br.edu.ufape.alugafacil.dto.PlanDto;
import java.time.LocalDate;
import lombok.Data;


@Data
public class SubscriptionDto {


    private String subscriptionId;
    private LocalDate startDate;
    private LocalDate nextBillingDate;
    private PaymentStatus status;
    private PlanDto planDto;
}