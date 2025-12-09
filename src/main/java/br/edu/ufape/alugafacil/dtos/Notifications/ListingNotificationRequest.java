package br.edu.ufape.alugafacil.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ListingNotificationRequest {
    @NotBlank
    private String propertyId;
    @NotBlank
    private String alertName;
}