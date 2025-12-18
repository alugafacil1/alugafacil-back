package br.edu.ufape.alugafacil.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationResponse;
import br.edu.ufape.alugafacil.dtos.notifications.MessageNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.MessageNotificationResponse;
import br.edu.ufape.alugafacil.dtos.notifications.NotificationResponse;
import br.edu.ufape.alugafacil.mappers.NotificationMapper;
import br.edu.ufape.alugafacil.models.ListingNotification;
import br.edu.ufape.alugafacil.models.MessageNotification;
import br.edu.ufape.alugafacil.models.Notification;
import br.edu.ufape.alugafacil.repositories.NotificationRepository;
import br.edu.ufape.alugafacil.services.interfaces.INotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final FCMService fcmService;

    @Override
    @Transactional
    public ListingNotificationResponse createListingNotification(ListingNotificationRequest request) {
        // 1. Converter DTO -> Entity (MapStruct já preenche title e message)
        ListingNotification notification = notificationMapper.toEntity(request);
        
        // 2. Salvar
        ListingNotification saved = notificationRepository.save(notification);

        // 3. Enviar Push Notification (FCM)
        if (request.getTargetToken() != null && !request.getTargetToken().isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "LISTING");
            data.put("notificationId", saved.getNotificationId().toString());
            data.put("propertyId", request.getPropertyId().toString());

            fcmService.sendNotification(request.getTargetToken(), saved.getTitle(), saved.getMessage(), data);
        }

        // 4. Converter Entity -> DTO Response
        return notificationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public MessageNotificationResponse createMessageNotification(MessageNotificationRequest request) {
        
        MessageNotification notification = notificationMapper.toEntity(request);

        
        MessageNotification saved = notificationRepository.save(notification);

        
        if (request.getTargetToken() != null && !request.getTargetToken().isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "MESSAGE");
            data.put("notificationId", saved.getNotificationId().toString());
            if (request.getConversationId() != null) {
                data.put("conversationId", request.getConversationId().toString());
            }
            data.put("senderName", request.getSenderName());

            fcmService.sendNotification(request.getTargetToken(), saved.getTitle(), saved.getMessage(), data);
        }

        
        return notificationMapper.toResponse(saved);
    }

    @Override
    public Page<NotificationResponse> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable)
                .map(notificationMapper::toResponse);
    }

    @Override
    public NotificationResponse getNotificationById(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada com ID: " + id));
        
        return notificationMapper.toResponse(notification);
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada com ID: " + id));
        
        notification.setRead(true);
        Notification saved = notificationRepository.save(notification);
        
        return notificationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteNotification(UUID id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notificação não encontrada com ID: " + id);
        }
        notificationRepository.deleteById(id);
    }
}