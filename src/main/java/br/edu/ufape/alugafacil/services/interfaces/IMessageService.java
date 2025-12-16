package br.edu.ufape.alugafacil.services.interfaces;

import br.edu.ufape.alugafacil.dtos.message.MessageRequest;
import br.edu.ufape.alugafacil.models.Message;

import java.util.List;
import java.util.UUID;

public interface IMessageService {
    Message sendMessage(MessageRequest dto);
    List<Message> getMessagesByConversation(UUID conversationId);
    void markAsRead(UUID messageId);
}