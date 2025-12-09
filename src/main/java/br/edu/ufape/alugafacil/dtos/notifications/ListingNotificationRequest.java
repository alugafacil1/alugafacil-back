package br.edu.ufape.alugafacil.dtos.notifications;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ListingNotificationRequest {
    @NotBlank
    private UUID propertyId;
    @NotBlank
    private String alertName;
}