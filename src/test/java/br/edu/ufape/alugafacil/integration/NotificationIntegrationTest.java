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
import br.edu.ufape.alugafacil.models.MessageNotification;
import br.edu.ufape.alugafacil.models.Notification;
import br.edu.ufape.alugafacil.repositories.NotificationRepository;
import br.edu.ufape.alugafacil.services.FCMService;
import br.edu.ufape.alugafacil.services.NotificationService;
import br.edu.ufape.alugafacil.services.interfaces.IFileStorageService;
import br.edu.ufape.alugafacil.services.interfaces.IPropertyService;

@SpringBootTest
@ActiveProfiles("test") // Usa o banco H2 configurado no application-test.properties
class NotificationIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    // --- MOCKS DE DEPENDÊNCIAS EXTERNAS ---
    // Estes mocks são necessários para que o ApplicationContext do Spring suba
    // sem tentar conectar em serviços reais ou quebrar por falta de dependência.

    @MockBean
    private FCMService fcmService; // Mocka o Firebase para não enviar push real

    @MockBean
    private IFileStorageService fileStorageService; // Mocka o Storage para destrava o PropertyService

    
    @MockBean
    private IPropertyService propertyService; 
    
    // ------------------------------------------

    @Test
    @DisplayName("Deve persistir uma notificação de mensagem no banco H2")
    void shouldPersistMessageNotificationInDatabase() {
        // 1. Cenário (Arrange)
        MessageNotificationRequest request = new MessageNotificationRequest();
        request.setConversationId(UUID.randomUUID());
        request.setSenderName("João da Silva");
        request.setTargetToken("token-fake-123");

        // 2. Ação (Act)
        // Aqui chamamos o serviço real, que vai chamar o Repository real e salvar no H2
        MessageNotificationResponse response = notificationService.createMessageNotification(request);

        // 3. Verificação (Assert)
        
        // Verifica se o serviço retornou um ID (sinal que salvou)
        assertNotNull(response.getId(), "O ID da resposta não deveria ser nulo");
        
        // Vai no banco de dados verificar se o registro existe mesmo
        Notification saved = notificationRepository.findById(response.getId()).orElse(null);
        
        assertNotNull(saved, "A notificação deveria ter sido encontrada no banco de dados");
        assertTrue(saved instanceof MessageNotification, "Deveria ser uma instância de MessageNotification");
        assertEquals("João da Silva", ((MessageNotification) saved).getSenderName());
        assertEquals("Nova Mensagem", saved.getTitle()); // Verifica se o Mapper funcionou
    }
}