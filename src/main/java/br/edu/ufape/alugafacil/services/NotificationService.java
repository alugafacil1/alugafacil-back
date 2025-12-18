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
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.NotificationRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.services.interfaces.INotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final FCMService fcmService;
    private final UserRepository userRepository; 

    
    @Override
    @Transactional
    public ListingNotificationResponse createListingNotification(ListingNotificationRequest request) {
        ListingNotification notification = notificationMapper.toEntity(request);
        ListingNotification saved = notificationRepository.save(notification);

        if (request.getTargetToken() != null && !request.getTargetToken().isEmpty()) {
            sendListingPush(request.getTargetToken(), saved, request.getPropertyId());
        }
        return notificationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public MessageNotificationResponse createMessageNotification(MessageNotificationRequest request) {
        MessageNotification notification = notificationMapper.toEntity(request);
        MessageNotification saved = notificationRepository.save(notification);

        if (request.getTargetToken() != null && !request.getTargetToken().isEmpty()) {
            sendMessagePush(request.getTargetToken(), saved, request.getConversationId(), request.getSenderName());
        }
        return notificationMapper.toResponse(saved);
    }


    @Override
    @Transactional
    public void notifyListingMatch(UUID userId, UUID propertyId, String alertName) {
        // 1. Buscar usuário
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para notificação"));

        // 2. Criar objeto de Request interno para aproveitar o Mapper
        ListingNotificationRequest request = new ListingNotificationRequest();
        request.setPropertyId(propertyId);
        request.setAlertName(alertName);

        // 3. Converter e Associar User
        ListingNotification notification = notificationMapper.toEntity(request);
        notification.setUser(user); 

        // 4. Salvar no histórico
        ListingNotification saved = notificationRepository.save(notification);

        // 5. Enviar Push (se o usuário tiver token)
        if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
            sendListingPush(user.getFcmToken(), saved, propertyId);
        }
    }

    @Override
    @Transactional
    public void notifyNewMessage(UUID recipientId, UUID conversationId, String senderName) {
        
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Usuário destinatário não encontrado"));

        
        MessageNotificationRequest request = new MessageNotificationRequest();
        request.setConversationId(conversationId);
        request.setSenderName(senderName);

        
        MessageNotification notification = notificationMapper.toEntity(request);
        notification.setUser(recipient);

        MessageNotification saved = notificationRepository.save(notification);

        if (recipient.getFcmToken() != null && !recipient.getFcmToken().isEmpty()) {
            sendMessagePush(recipient.getFcmToken(), saved, conversationId, senderName);
        }
    }


    private void sendListingPush(String token, ListingNotification notification, UUID propertyId) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "LISTING");
        data.put("notificationId", notification.getNotificationId().toString()); 
        data.put("propertyId", propertyId.toString());

        fcmService.sendNotification(token, notification.getTitle(), notification.getMessage(), data);
    }

    private void sendMessagePush(String token, MessageNotification notification, UUID conversationId, String senderName) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "MESSAGE");
        data.put("notificationId", notification.getNotificationId().toString()); 
        if (conversationId != null) {
            data.put("conversationId", conversationId.toString());
        }
        data.put("senderName", senderName);

        fcmService.sendNotification(token, notification.getTitle(), notification.getMessage(), data);
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