package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.models.ListingNotification;
import br.edu.ufape.alugafacil.models.MessageNotification;
import br.edu.ufape.alugafacil.models.Notification;
import br.edu.ufape.alugafacil.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public ListingNotification createListingNotification(UUID propertyId, String alertName) {
        ListingNotification notification = new ListingNotification();
        notification.setTitle("New Property Listed!");
        notification.setMessage("A property matching your alert has been listed.");
        notification.setPropertyId(propertyId);
        
        notification.setAlertName(alertName);
        
        return notificationRepository.save(notification);
    }

    public MessageNotification createMessageNotification(UUID conversationId, String senderName) {
        MessageNotification notification = new MessageNotification();
        notification.setTitle("New Message");
        notification.setMessage("You received a message from " + senderName);
        
        if (conversationId != null) {
            notification.setConversationId(conversationId);
        }
        
        notification.setSenderName(senderName);

        return notificationRepository.save(notification);
    }


    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));
    }

    public Notification markAsRead(UUID id) {
        Notification notification = getNotificationById(id);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }


    public void deleteNotification(UUID id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with ID: " + id);
        }
        notificationRepository.deleteById(id);
    }
}