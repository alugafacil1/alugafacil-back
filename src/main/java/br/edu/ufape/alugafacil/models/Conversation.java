package br.edu.ufape.alugafacil.models;

import java.util.List;
import java.util.UUID;

import br.edu.ufape.alugafacil.models.enums.ConversationType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID conversationId;

    @ManyToOne
    @JoinColumn(name = "initiator_user_id")
    private User initiatorUser;
    
    @ManyToOne
    @JoinColumn(name = "recipient_user_id")
    private User recipientUser;
    
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;
    
    private java.time.LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ConversationType type;

    @OneToMany(mappedBy = "conversation")
    private List<Message> messages;
}
