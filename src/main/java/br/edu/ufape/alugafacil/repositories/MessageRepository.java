package br.edu.ufape.alugafacil.repositories;

import br.edu.ufape.alugafacil.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByConversationConversationIdOrderByCreatedAtAsc(String conversationId);
}