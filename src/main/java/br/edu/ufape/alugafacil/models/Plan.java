package br.edu.ufape.alugafacil.models;

import java.util.UUID;

import br.edu.ufape.alugafacil.models.enums.PlanType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID planId;

    private String name;
    private Integer priceInCents;
    private Boolean hasVideo;
    private Integer imagesCount;
    private Integer propertiesCount;
    private Boolean isPriority;
    private Boolean hasNotification;

    @Enumerated(EnumType.STRING)
    private PlanType planType;
}