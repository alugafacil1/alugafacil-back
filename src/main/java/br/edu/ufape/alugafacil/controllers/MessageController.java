package br.edu.ufape.alugafacil.controllers;

import br.edu.ufape.alugafacil.dtos.message.MessageRequest;
import br.edu.ufape.alugafacil.models.Message;
import br.edu.ufape.alugafacil.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> create(@Valid @RequestBody MessageRequest dto) {
        Message newMessage = messageService.sendMessage(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMessage);
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> listByConversation(@PathVariable String conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversation(conversationId));
    }
    
    @PatchMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String messageId) {
        messageService.markAsRead(messageId);
        return ResponseEntity.noContent().build();
    }
}