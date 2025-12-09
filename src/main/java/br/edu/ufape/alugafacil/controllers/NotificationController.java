package br.edu.ufape.alugafacil.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationResponse;
import br.edu.ufape.alugafacil.dtos.notifications.MessageNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.MessageNotificationResponse;
import br.edu.ufape.alugafacil.dtos.notifications.NotificationResponse;
import br.edu.ufape.alugafacil.models.ListingNotification;
import br.edu.ufape.alugafacil.models.MessageNotification;
import br.edu.ufape.alugafacil.models.Notification;
import br.edu.ufape.alugafacil.services.NotificationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*") 
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ModelMapper modelMapper; 


    @PostMapping("/listing")
    public ResponseEntity<ListingNotificationResponse> createListingNotification(@Valid @RequestBody ListingNotificationRequest request) {
        ListingNotification entity = notificationService.createListingNotification(
                request.getPropertyId(),
                request.getAlertName()
        );
        return new ResponseEntity<>((ListingNotificationResponse) convertToDto(entity), HttpStatus.CREATED);
    }

    @PostMapping("/message")
    public ResponseEntity<MessageNotificationResponse> createMessageNotification(@Valid @RequestBody MessageNotificationRequest request) {
        MessageNotification entity = notificationService.createMessageNotification(
                request.getConversationId(),
                request.getSenderName()
        );
        return new ResponseEntity<>((MessageNotificationResponse) convertToDto(entity), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<Notification> entities = notificationService.getAllNotifications();
        
        
        List<NotificationResponse> dtos = entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        Notification entity = notificationService.getNotificationById(id);
        return ResponseEntity.ok(convertToDto(entity));
    }


    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        Notification entity = notificationService.markAsRead(id);
        return ResponseEntity.ok(convertToDto(entity));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    
    private NotificationResponse convertToDto(Notification entity) {
    NotificationResponse dto;
    
    if (entity instanceof ListingNotification) {
        ListingNotification ln = (ListingNotification) entity;
        ListingNotificationResponse resp = new ListingNotificationResponse();
        resp.setPropertyId(ln.getPropertyId());
        resp.setAlertName(ln.getAlertName());
        dto = resp;
    } else {
        MessageNotification mn = (MessageNotification) entity;
        MessageNotificationResponse resp = new MessageNotificationResponse();
        resp.setConversationId(mn.getConversationId());
        resp.setSenderName(mn.getSenderName());
        dto = resp;
    }

    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setBody(entity.getBody());
    dto.setRead(entity.isRead());
    dto.setCreatedAt(entity.getCreatedAt());
    
    return dto;
}
}