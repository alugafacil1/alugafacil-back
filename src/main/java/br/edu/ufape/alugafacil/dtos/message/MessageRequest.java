package br.edu.ufape.alugafacil.dtos.message;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {
    @NotBlank(message = "{validation.required}")
    private UUID conversationId;
    
    @NotBlank(message = "{validation.required}")
    private UUID senderId;
    
    @NotBlank(message = "{validation.not.blank}")
    private String content;
}