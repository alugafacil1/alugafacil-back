package br.edu.ufape.alugafacil.dtos.notifications;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageNotificationRequest {
    
    @NotNull(message = "O ID da conversa é obrigatório") 
    private UUID conversationId;

    @NotBlank(message = "O nome do remetente é obrigatório")
    private String senderName;

    private String targetToken;
}