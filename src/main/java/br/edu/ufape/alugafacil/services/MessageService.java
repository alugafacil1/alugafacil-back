package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.dtos.message.MessageRequest;
import br.edu.ufape.alugafacil.models.Conversation;
import br.edu.ufape.alugafacil.models.Message;
import br.edu.ufape.alugafacil.repositories.ConversationRepository;
import br.edu.ufape.alugafacil.repositories.MessageRepository;
import br.edu.ufape.alugafacil.services.interfaces.IMessageService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    
    @Override
    public Message sendMessage(MessageRequest dto) {
        Conversation conversation = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversa não encontrada"));

        Message message = new Message();
        
        message.setSenderId(dto.getSenderId());
        
        message.setContent(dto.getContent());
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(false);
        message.setConversation(conversation);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByConversation(UUID conversationId) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new RuntimeException("Conversa não encontrada");
        }
        
        // Passa o UUID para o repositório
        return messageRepository.findByConversationConversationIdOrderByCreatedAtAsc(conversationId);
    }
    
    @Override
    public void markAsRead(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
        
        message.setRead(true);
        messageRepository.save(message);
    }
}