package br.edu.ufape.alugafacil.dtos;

import java.util.UUID;

import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.models.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponseDTO {
    private UUID planId;
    private String name;
    private Integer priceInCents;
    private Boolean hasVideo;
    private Integer imagesCount;
    private Integer propertiesCount;
    private Boolean isPriority;
    private Boolean hasNotification;
    private PlanType planType;

    public PlanResponseDTO(Plan plan) {
        this.planId = plan.getPlanId();
        this.name = plan.getName();
        this.priceInCents = plan.getPriceInCents();
        this.hasVideo = plan.getHasVideo();
        this.imagesCount = plan.getImagesCount();
        this.propertiesCount = plan.getPropertiesCount();
        this.isPriority = plan.getIsPriority();
        this.hasNotification = plan.getHasNotification();
        this.planType = plan.getPlanType();
    }
}
