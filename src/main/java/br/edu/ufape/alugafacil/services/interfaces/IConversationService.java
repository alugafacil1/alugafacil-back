package br.edu.ufape.alugafacil.services.interfaces;

import br.edu.ufape.alugafacil.dtos.conversation.ConversationRequest;
import br.edu.ufape.alugafacil.models.Conversation;
import java.util.List;

public interface IConversationService {
    Conversation createConversation(ConversationRequest dto);
    Conversation getConversationById(String id);
    List<Conversation> getConversationsByUser(String userId);
    List<Conversation> getAllConversations();
    void deleteConversation(String id);
}