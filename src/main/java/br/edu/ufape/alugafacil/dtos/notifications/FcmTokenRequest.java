package br.edu.ufape.alugafacil.dtos.notifications;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FcmTokenRequest {
    @NotBlank(message = "O token não pode estar vazio")
    private String token;
}