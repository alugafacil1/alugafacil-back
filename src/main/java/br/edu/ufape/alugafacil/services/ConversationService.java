package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.dtos.conversation.ConversationRequest;
import br.edu.ufape.alugafacil.exceptions.ResourceNotFoundException;
import br.edu.ufape.alugafacil.models.Conversation;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.ConversationRepository;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository; 
import br.edu.ufape.alugafacil.services.interfaces.IConversationService;
import br.edu.ufape.alugafacil.services.interfaces.INotificationService; 
import br.edu.ufape.alugafacil.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j 
public class ConversationService implements IConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository; 
    private final PropertyRepository propertyRepository;
    private final INotificationService notificationService; 
    private final MessageUtils messages;

    @Override
    public Conversation createConversation(ConversationRequest dto) {
        
        User initiator = userRepository.findById(dto.getInitiatorUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    messages.getMessage("user.initiator.not.found", dto.getInitiatorUserId())
                ));
        
        User recipient = userRepository.findById(dto.getRecipientUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    messages.getMessage("user.recipient.not.found", dto.getRecipientUserId())
                ));
        
        Property property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    messages.getMessage("property.not.found", dto.getPropertyId())
                ));

        Conversation conversation = new Conversation();
        conversation.setInitiatorUser(initiator);
        conversation.setRecipientUser(recipient); 
        conversation.setProperty(property);
        conversation.setType(dto.getType());
        conversation.setCreatedAt(LocalDateTime.now());

        Conversation savedConversation = conversationRepository.save(conversation);

        try {
            notificationService.notifyNewMessage(
                recipient.getUserId(),      
                savedConversation.getConversationId(),  
                initiator.getName()        
            );
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de nova conversa: {}", e.getMessage());
        }

        return savedConversation;
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public Conversation getConversationById(UUID id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    messages.getMessage("conversation.not.found", id)
                ));
    }

    public List<Conversation> getConversationsByUser(UUID userId) {
        return conversationRepository.findByUserId(userId);
    }

    public void deleteConversation(UUID id) {
        conversationRepository.deleteById(id);
    }
}