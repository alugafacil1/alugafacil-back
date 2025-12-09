package br.edu.ufape.alugafacil.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "notification_type")
@Data
public abstract class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;

    private String title;

    private String message;
    
    private String imageUrl;

    private java.time.LocalDateTime createdAt;
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    public void prepareForSave() {
        if (this.createdAt == null) {
            this.createdAt = java.time.LocalDateTime.now();
        }
    }
}