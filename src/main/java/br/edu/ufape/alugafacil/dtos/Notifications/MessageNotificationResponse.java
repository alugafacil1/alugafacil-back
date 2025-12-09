package br.edu.ufape.alugafacil.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageNotificationResponse extends NotificationResponse {
    private String conversationId;
    private String senderName;

    public MessageNotificationResponse() {
        this.setType("MESSAGE");
    }
}