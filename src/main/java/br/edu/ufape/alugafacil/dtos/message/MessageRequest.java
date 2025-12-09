package br.edu.ufape.alugafacil.dtos.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {
    
    @NotBlank(message = "{validation.required}")
    private String conversationId;
    
    @NotBlank(message = "{validation.required}")
    private String senderId;
    
    @NotBlank(message = "{validation.not.blank}")
    private String content;
}