package br.edu.ufape.alugafacil.services.interfaces;

import br.edu.ufape.alugafacil.models.ListingNotification;
import br.edu.ufape.alugafacil.models.MessageNotification;
import br.edu.ufape.alugafacil.models.Notification;
import java.util.List;
import java.util.UUID;

public interface INotificationService {
    ListingNotification createListingNotification(UUID propertyId, String alertName, String targetFcmToken);
    MessageNotification createMessageNotification(UUID conversationId, String senderName, String targetFcmToken);
    List<Notification> getAllNotifications();
    Notification getNotificationById(UUID id);
    Notification markAsRead(UUID id);
    void deleteNotification(UUID id);
}
