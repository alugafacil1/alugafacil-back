package br.edu.ufape.alugafacil.dtos.notifications;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public abstract class NotificationResponse {
    private UUID id;
    private String title;
    private String body;
    private boolean read;
    private LocalDateTime createdAt;
    private String type;
}