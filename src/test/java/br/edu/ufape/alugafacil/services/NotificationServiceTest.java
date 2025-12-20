package br.edu.ufape.alugafacil.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationRequest;
import br.edu.ufape.alugafacil.dtos.notifications.ListingNotificationResponse;
import br.edu.ufape.alugafacil.mappers.NotificationMapper;
import br.edu.ufape.alugafacil.models.ListingNotification;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.ConversationRepository; // <--- Import Novo
import br.edu.ufape.alugafacil.repositories.NotificationRepository;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;     // <--- Import Novo
import br.edu.ufape.alugafacil.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private FCMService fcmService;

    @Mock
    private UserRepository userRepository;
    
    // --- ADICIONE ESTES MOCKS ---
    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private ConversationRepository conversationRepository;
    // ---------------------------

    @Test
    @DisplayName("Deve criar notificação de imóvel e enviar Push quando token existir")
    void shouldCreateListingNotificationAndSendPush() {
        // 1. Cenário (Arrange)
        UUID propertyId = UUID.randomUUID();
        String token = "token-dummy-123";
        
        ListingNotificationRequest request = new ListingNotificationRequest();
        request.setPropertyId(propertyId);
        request.setAlertName("Apê Centro");
        request.setTargetToken(token);

        ListingNotification entity = new ListingNotification();
        entity.setNotificationId(UUID.randomUUID()); // Ajuste se seu modelo usa setId ou setNotificationId
        entity.setTitle("Novo Imóvel!");
        
        // --- COMPORTAMENTO NECESSÁRIO PARA PASSAR NA VALIDAÇÃO ---
        when(propertyRepository.existsById(propertyId)).thenReturn(true);
        // ---------------------------------------------------------
        
        // Mockando Mapper e Repository
        when(notificationMapper.toEntity(request)).thenReturn(entity);
        when(notificationRepository.save(any(ListingNotification.class))).thenReturn(entity);
        when(notificationMapper.toResponse(entity)).thenReturn(new ListingNotificationResponse());

        // 2. Ação (Act)
        notificationService.createListingNotification(request);

        // 3. Verificação (Assert)
        verify(propertyRepository).existsById(propertyId); // Verifica se validou
        verify(notificationRepository, times(1)).save(entity);
        
        // Verifica se o FCM foi chamado pois passamos um token
        verify(fcmService, times(1)).sendNotification(eq(token), any(), any(), any());
    }

    @Test
    @DisplayName("Trigger Interno: Deve notificar match de imóvel buscando usuário no banco")
    void shouldNotifyListingMatchInternal() {
        // 1. Cenário
        UUID userId = UUID.randomUUID();
        UUID propertyId = UUID.randomUUID();
        
        // Usando o construtor padrão e setters se o Builder não estiver disponível
        User user = new User(); 
        user.setUserId(userId); // ou setUserId(userId)
        user.setFcmToken("token-do-usuario");
        
        ListingNotification entity = new ListingNotification();
        entity.setNotificationId(UUID.randomUUID());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationMapper.toEntity(any(ListingNotificationRequest.class))).thenReturn(entity);
        when(notificationRepository.save(any(ListingNotification.class))).thenReturn(entity);

        // 2. Ação
        notificationService.notifyListingMatch(userId, propertyId, "Alerta Barato");

        // 3. Verificação
        verify(userRepository).findById(userId);
        verify(notificationRepository).save(entity);
        
        // Verifica se o setUser foi chamado (vinculou a notificação ao usuário)
        assertEquals(user, entity.getUser()); 
        
        // Verifica se enviou o push usando o token que veio do User (e não do request)
        verify(fcmService).sendNotification(eq("token-do-usuario"), any(), any(), any());
    }
}