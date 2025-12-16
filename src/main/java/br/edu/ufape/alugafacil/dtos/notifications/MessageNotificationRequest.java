package br.edu.ufape.alugafacil.dtos.notifications;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageNotificationRequest {
    @NotBlank
    private UUID conversationId;
    @NotBlank
    private String senderName;
}