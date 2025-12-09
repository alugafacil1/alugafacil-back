package br.edu.ufape.alugafacil.dto;

import br.edu.ufape.alugafacil.enums.PlanType;

import lombok.Data;


@Data
public class PlanDto {

    private String planId;
    private String name;
    private Integer priceInCents;
    private Boolean hasVideo;
    private Integer imagesCount;
    private Integer propertiesCount;
    private Boolean isPriority;
    private Boolean hasNotification;
    private PlanType planType;
}