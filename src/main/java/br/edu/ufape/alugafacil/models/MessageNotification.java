package br.edu.ufape.alugafacil.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("MESSAGE")
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageNotification extends Notification {
    private String conversationId;
    private String senderName;
}
