package br.edu.ufape.alugafacil.controllers;

import br.edu.ufape.alugafacil.dtos.conversation.ConversationRequest;
import br.edu.ufape.alugafacil.models.Conversation;
import br.edu.ufape.alugafacil.services.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<Conversation> create(@Valid @RequestBody ConversationRequest dto) {
        Conversation newConversation = conversationService.createConversation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newConversation);
    }

    @GetMapping
    public ResponseEntity<List<Conversation>> listAll() {
        return ResponseEntity.ok(conversationService.getAllConversations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> findById(@PathVariable String id) {
        return ResponseEntity.ok(conversationService.getConversationById(id));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> listByUser(@PathVariable String userId) {
        return ResponseEntity.ok(conversationService.getConversationsByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}