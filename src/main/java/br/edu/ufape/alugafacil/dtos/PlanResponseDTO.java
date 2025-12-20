package br.edu.ufape.alugafacil.dtos;

import java.util.UUID;

import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.models.Plan;
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
    private UserType targetAudience;

    public PlanResponseDTO(Plan plan) {

        this.name = plan.getName();
        this.priceInCents = plan.getPriceInCents();
        this.hasVideo = plan.getHasVideo();
        this.imagesCount = plan.getImagesCount();
        this.propertiesCount = plan.getPropertiesCount();
        this.isPriority = plan.getIsPriority();
        this.hasNotification = plan.getHasNotification();
        this.targetAudience = plan.getTargetAudience();
    }
}
