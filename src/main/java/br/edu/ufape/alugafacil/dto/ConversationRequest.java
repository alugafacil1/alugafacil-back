package br.edu.ufape.alugafacil.dto;

import br.edu.ufape.alugafacil.models.enums.ConversationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConversationRequest {

    @NotBlank(message = "{validation.required}")
    private String initiatorUserId;

    @NotBlank(message = "{validation.required}")
    private String recipientUserId;

    @NotBlank(message = "{validation.required}")
    private String propertyId;

    @NotNull(message = "{validation.required}")
    private ConversationType type;
}