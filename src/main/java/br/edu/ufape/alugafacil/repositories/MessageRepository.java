package br.edu.ufape.alugafacil.repositories;

import br.edu.ufape.alugafacil.models.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByConversationConversationIdOrderByCreatedAtAsc(UUID conversationId);
}