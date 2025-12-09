package br.edu.ufape.alugafacil.dtos.notifications;

import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageNotificationResponse extends NotificationResponse {
    private UUID conversationId;
    private String senderName;

    public MessageNotificationResponse() {
        this.setType("MESSAGE");
    }
}