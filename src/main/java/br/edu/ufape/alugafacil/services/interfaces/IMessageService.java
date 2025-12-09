package br.edu.ufape.alugafacil.services.interfaces;

import br.edu.ufape.alugafacil.dtos.message.MessageRequest;
import br.edu.ufape.alugafacil.models.Message;
import java.util.List;

public interface IMessageService {
    Message sendMessage(MessageRequest dto);
    List<Message> getMessagesByConversation(String conversationId);
    void markAsRead(String messageId);
}