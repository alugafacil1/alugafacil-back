package br.edu.ufape.alugafacil.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageNotificationRequest {
    @NotBlank
    private String conversationId;
    @NotBlank
    private String senderName;
}