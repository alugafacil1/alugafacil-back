package br.edu.ufape.alugafacil.repositories;

import br.edu.ufape.alugafacil.models.Conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("SELECT c FROM Conversation c WHERE c.initiatorUser.userId = :userId OR c.recipientUser.userId = :userId")
    List<Conversation> findByUserId(@Param("userId") UUID userId);
}