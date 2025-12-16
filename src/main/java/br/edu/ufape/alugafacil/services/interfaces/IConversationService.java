package br.edu.ufape.alugafacil.services.interfaces;

import br.edu.ufape.alugafacil.dtos.conversation.ConversationRequest;
import br.edu.ufape.alugafacil.models.Conversation;

import java.util.List;
import java.util.UUID;

public interface IConversationService {
    Conversation createConversation(ConversationRequest dto);
    Conversation getConversationById(UUID id);
    List<Conversation> getConversationsByUser(UUID userId);
    List<Conversation> getAllConversations();
    void deleteConversation(UUID id);
}