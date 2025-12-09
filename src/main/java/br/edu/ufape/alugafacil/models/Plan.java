package br.edu.ufape.alugafacil.models;

import br.edu.ufape.alugafacil.dto.PlanDto;
import br.edu.ufape.alugafacil.enums.PlanType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String planId;
    private String name;
    private Integer priceInCents;
    private Boolean hasVideo;
    private Integer imagesCount;
    private Integer propertiesCount;
    private Boolean isPriority;
    private Boolean hasNotification;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    public static Plan getEntity(PlanDto planDto) {
        Plan plan = Plan.builder()
                .planId(planDto.getPlanId() != null && !planDto.getPlanId().isEmpty() ? planDto.getPlanId() : null)
                .name(planDto.getName())
                .hasNotification(planDto.getHasNotification())
                .priceInCents(planDto.getPriceInCents())
                .hasVideo(planDto.getHasVideo())
                .imagesCount(planDto.getImagesCount())
                .isPriority(planDto.getIsPriority())
                .planType(planDto.getPlanType())
                .build();
        return  plan;
    }
}