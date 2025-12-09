package br.edu.ufape.alugafacil.dtos.conversation;

import java.util.UUID;

import br.edu.ufape.alugafacil.models.enums.ConversationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConversationRequest {

    @NotBlank(message = "{validation.required}")
    private UUID initiatorUserId;

    @NotBlank(message = "{validation.required}")
    private UUID recipientUserId;

    @NotBlank(message = "{validation.required}")
    private UUID propertyId;

    @NotNull(message = "{validation.required}")
    private ConversationType type;
}