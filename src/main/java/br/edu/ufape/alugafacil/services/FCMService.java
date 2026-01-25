package br.edu.ufape.alugafacil.services;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import br.edu.ufape.alugafacil.services.interfaces.IFCMService;

import java.util.Map;

@Service
public class FCMService implements IFCMService{

    private static final Logger logger = LoggerFactory.getLogger(FCMService.class);

    public void sendNotification(String targetToken, String title, String body, Map<String, String> data) {
        if (targetToken == null || targetToken.isEmpty()) {
            logger.warn("Token FCM não fornecido. Notificação não enviada para o mobile.");
            return;
        }

        // Verifica se o Firebase está inicializado
        if (FirebaseApp.getApps().isEmpty()) {
            logger.warn("Firebase não está configurado. Notificação não enviada. Configure o serviceAccountKey.json para habilitar notificações push.");
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
            logger.info("Notificação enviada com sucesso: {}", response);
        } catch (Exception e) {
            logger.error("Erro ao enviar notificação FCM: {}", e.getMessage(), e);
        }
    }
}