package br.edu.ufape.alugafacil.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String messageId;

    private String senderId;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private java.time.LocalDateTime createdAt;
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
}
