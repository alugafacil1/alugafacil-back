package br.edu.ufape.alugafacil.dtos.notifications;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public abstract class NotificationResponse {
    private Long id;
    private String title;
    private String body;
    private boolean read;
    private LocalDateTime createdAt;
    private String type;
}