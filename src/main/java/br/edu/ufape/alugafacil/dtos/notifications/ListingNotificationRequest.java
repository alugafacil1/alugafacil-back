package br.edu.ufape.alugafacil.dtos.notifications;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ListingNotificationRequest {
    @NotNull(message = "O ID do imóvel é obrigatório")
    private UUID propertyId;
    @NotBlank(message = "O nome do alerta é obrigatório")
    private String alertName;
}