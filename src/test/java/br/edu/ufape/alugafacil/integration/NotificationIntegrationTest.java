package br.edu.ufape.alugafacil.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import br.edu.ufape.alugafacil.dtos.notifications.MessageNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.MessageNotificationResponse;
import br.edu.ufape.alugafacil.models.Conversation;
import br.edu.ufape.alugafacil.models.MessageNotification;
import br.edu.ufape.alugafacil.models.Notification;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.ConversationRepository;
import br.edu.ufape.alugafacil.repositories.NotificationRepository;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.services.FCMService;
import br.edu.ufape.alugafacil.services.NotificationService;
import br.edu.ufape.alugafacil.services.interfaces.IFileStorageService;
import br.edu.ufape.alugafacil.services.interfaces.IPropertyService;

@SpringBootTest
@ActiveProfiles("test")
class NotificationIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    // --- NOVOS REPOSITÓRIOS PARA O CENÁRIO ---
    @Autowired private UserRepository userRepository;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private ConversationRepository conversationRepository;
    // -----------------------------------------

    @MockBean
    private FCMService fcmService;

    @MockBean
    private IFileStorageService fileStorageService;

    @MockBean
    private IPropertyService propertyService;

    @Test
    @DisplayName("Deve persistir uma notificação de mensagem no banco H2")
    void shouldPersistMessageNotificationInDatabase() {
        // 1. Cenário (Arrange) - Criando os dados reais no H2
        
        // Criar Usuários
        User sender = new User();
        sender.setEmail("sender@test.com"); // Campos obrigatórios mínimos
        userRepository.save(sender);

        User recipient = new User();
        recipient.setEmail("recipient@test.com");
        recipient.setFcmToken("token-fake-123"); // Importante se o service validar isso
        userRepository.save(recipient);

        // Criar Imóvel
        Property property = new Property();
        property.setUser(recipient); // O dono é o destinatário
        propertyRepository.save(property);

        // Criar Conversa (Obrigatório para passar na validação do Service)
        Conversation conversation = new Conversation();
        conversation.setInitiatorUser(sender);
        conversation.setRecipientUser(recipient);
        conversation.setProperty(property);
        Conversation savedConversation = conversationRepository.save(conversation);

        // Montar Request com o ID REAL da conversa salva
        MessageNotificationRequest request = new MessageNotificationRequest();
        request.setConversationId(savedConversation.getConversationId());
        request.setSenderName("João da Silva");
        request.setTargetToken("token-fake-123");

        // 2. Ação (Act)
        MessageNotificationResponse response = notificationService.createMessageNotification(request);

        // 3. Verificação (Assert)
        assertNotNull(response.getId(), "O ID da resposta não deveria ser nulo");
        
        Notification saved = notificationRepository.findById(response.getId()).orElse(null);
        
        assertNotNull(saved, "A notificação deveria ter sido encontrada no banco de dados");
        assertTrue(saved instanceof MessageNotification, "Deveria ser uma instância de MessageNotification");
        assertEquals("João da Silva", ((MessageNotification) saved).getSenderName());
        
        // Verifica se a notificação foi vinculada ao usuário correto (destinatário da conversa)
        // Isso depende se o seu mapper/service preenche o 'user' na notificação
        if (saved.getUser() != null) {
             assertEquals(recipient.getUserId(), saved.getUser().getUserId());
        }
    }
}