package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.models.ListingNotification;
import br.edu.ufape.alugafacil.models.MessageNotification;
import br.edu.ufape.alugafacil.models.Notification;
import br.edu.ufape.alugafacil.services.interfaces.INotificationService;
import br.edu.ufape.alugafacil.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FCMService fcmService; // Injeta a INTERFACE, não a classe

    @Override
    public ListingNotification createListingNotification(UUID propertyId, String alertName, String targetFcmToken) {
        ListingNotification notification = new ListingNotification();
        notification.setTitle("New Property Listed!");
        notification.setMessage("A property matching your alert '" + alertName + "' has been listed.");
        notification.setPropertyId(propertyId);
        notification.setAlertName(alertName);
        
        ListingNotification saved = notificationRepository.save(notification);

        if (targetFcmToken != null && !targetFcmToken.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "LISTING");
            data.put("notificationId", saved.getNotificationId().toString());
            data.put("propertyId", propertyId.toString());

            fcmService.sendNotification(targetFcmToken, saved.getTitle(), saved.getMessage(), data);
        }
        
        return saved;
    }

    @Override
    public MessageNotification createMessageNotification(UUID conversationId, String senderName, String targetFcmToken) {
        MessageNotification notification = new MessageNotification();
        notification.setTitle("New Message");
        notification.setMessage("You received a message from " + senderName);
        
        if (conversationId != null) {
            notification.setConversationId(conversationId);
        }
        notification.setSenderName(senderName);

        MessageNotification saved = notificationRepository.save(notification);

        if (targetFcmToken != null && !targetFcmToken.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "MESSAGE");
            data.put("notificationId", saved.getNotificationId().toString());
            if (conversationId != null) {
                data.put("conversationId", conversationId.toString());
            }
            data.put("senderName", senderName);

            fcmService.sendNotification(targetFcmToken, saved.getTitle(), saved.getMessage(), data);
        }

        return saved;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));
    }

    @Override
    public Notification markAsRead(UUID id) {
        Notification notification = getNotificationById(id);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(UUID id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with ID: " + id);
        }
        notificationRepository.deleteById(id);
    }
}