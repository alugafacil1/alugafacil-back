package br.edu.ufape.alugafacil.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;
import br.edu.ufape.alugafacil.services.interfaces.IFCMService;

import java.util.Map;

@Service
public class FCMService implements IFCMService{

    public void sendNotification(String targetToken, String title, String body, Map<String, String> data) {
        if (targetToken == null || targetToken.isEmpty()) {
            System.out.println("Token FCM não fornecido. Notificação não enviada para o mobile.");
            return;
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message.Builder messageBuilder = Message.builder()
                .setToken(targetToken)
                .setNotification(notification);
        
        if (data != null) {
            messageBuilder.putAllData(data);
        }

        try {
            String response = FirebaseMessaging.getInstance().send(messageBuilder.build());
            System.out.println("Notificação enviada com sucesso: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}