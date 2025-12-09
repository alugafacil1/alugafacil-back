package br.edu.ufape.alugafacil.models;

import java.util.UUID;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("MESSAGE")
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageNotification extends Notification {
    private UUID conversationId;
    private UUID senderName;
}
