package br.edu.ufape.alugafacil.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionRequestDTO;
import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionResponseDTO;
import br.edu.ufape.alugafacil.enums.PaymentStatus;
import br.edu.ufape.alugafacil.enums.SubscriptionStatus;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.mappers.SubscriptionMapper;
import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.models.Subscription;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.PlanRepository;
import br.edu.ufape.alugafacil.repositories.SubscriptionRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock private SubscriptionRepository subscriptionRepository;
    @Mock private PlanRepository planRepository;
    @Mock private UserRepository userRepository;
    @Mock private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private User user;
    private Plan basicPlan;
    private Plan premiumPlan;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        user = new User();
        user.setUserId(userId);
        user.setUserType(UserType.OWNER);

        basicPlan = new Plan();
        basicPlan.setPlanId(UUID.randomUUID());
        basicPlan.setName("Plano Básico");
        basicPlan.setPriceInCents(0);
        basicPlan.setTargetAudience(UserType.OWNER);

        premiumPlan = new Plan();
        premiumPlan.setPlanId(UUID.randomUUID());
        premiumPlan.setName("Plano Premium");
        premiumPlan.setPriceInCents(9990);
        premiumPlan.setTargetAudience(UserType.OWNER);
    }

    @Test
    @DisplayName("Deve assinar um plano com sucesso (Primeira Assinatura)")
    void shouldSubscribeSuccessfullyFirstTime() {
        SubscriptionRequestDTO request = new SubscriptionRequestDTO(premiumPlan.getPlanId());
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(planRepository.findById(premiumPlan.getPlanId())).thenReturn(Optional.of(premiumPlan));
        
        when(subscriptionRepository.findFirstByUserUserIdAndStatus(eq(userId), any()))
            .thenReturn(Optional.empty());

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(i -> i.getArguments()[0]);
        
        SubscriptionResponseDTO mockResponse = new SubscriptionResponseDTO(
            UUID.randomUUID(), premiumPlan.getPlanId(), "Plano Premium", 9990, LocalDate.now(), LocalDate.now().plusDays(30), PaymentStatus.ACTIVE
        );
        when(subscriptionMapper.toResponse(any())).thenReturn(mockResponse);

        SubscriptionResponseDTO response = subscriptionService.subscribe(userId, request);

        assertNotNull(response);
        assertEquals("Plano Premium", response.planName());
        
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Deve fazer Upgrade de plano (Cancelar antigo e Assinar novo)")
    void shouldUpgradePlanSuccessfully() {
        SubscriptionRequestDTO request = new SubscriptionRequestDTO(premiumPlan.getPlanId());
        
        Subscription oldSub = new Subscription();
        oldSub.setPlan(basicPlan);
        oldSub.setStatus(PaymentStatus.ACTIVE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(planRepository.findById(premiumPlan.getPlanId())).thenReturn(Optional.of(premiumPlan));
        
        when(subscriptionRepository.findFirstByUserUserIdAndStatus(eq(userId), any()))
            .thenReturn(Optional.of(oldSub));

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(i -> i.getArguments()[0]);
        when(subscriptionMapper.toResponse(any())).thenReturn(mock(SubscriptionResponseDTO.class));

        subscriptionService.subscribe(userId, request);

        assertEquals(PaymentStatus.CANCELLED, oldSub.getStatus());
        verify(subscriptionRepository, times(2)).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Deve lançar erro se tentar assinar o mesmo plano que já possui")
    void shouldThrowErrorWhenSubscribingToSamePlan() {
        SubscriptionRequestDTO request = new SubscriptionRequestDTO(basicPlan.getPlanId());
        
        Subscription currentSub = new Subscription();
        currentSub.setPlan(basicPlan);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(planRepository.findById(basicPlan.getPlanId())).thenReturn(Optional.of(basicPlan));
        when(subscriptionRepository.findFirstByUserUserIdAndStatus(eq(userId), any()))
            .thenReturn(Optional.of(currentSub));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            subscriptionService.subscribe(userId, request);
        });

        assertEquals("Você já possui este plano ativo.", exception.getMessage());
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro se o plano for para outro público alvo (Ex: Tenant tentando comprar plano de Owner)")
    void shouldThrowErrorWhenTargetAudienceMismatch() {
        // Arrange
        Plan tenantPlan = new Plan();
        tenantPlan.setPlanId(UUID.randomUUID());
        tenantPlan.setTargetAudience(UserType.TENANT);

        SubscriptionRequestDTO request = new SubscriptionRequestDTO(tenantPlan.getPlanId());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user)); // User é OWNER
        when(planRepository.findById(tenantPlan.getPlanId())).thenReturn(Optional.of(tenantPlan));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            subscriptionService.subscribe(userId, request);
        });

        assertTrue(exception.getMessage().contains("Este plano não está disponível"));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve criar assinatura grátis inicial com sucesso")
    void shouldCreateInitialFreeSubscription() {
        when(planRepository.findFirstByPriceInCentsAndTargetAudience(0, UserType.OWNER))
            .thenReturn(Optional.of(basicPlan));

        subscriptionService.createInitialFreeSubscription(user);

        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao criar user se não existir plano grátis no banco")
    void shouldThrowErrorIfNoFreePlanExists() {
        when(planRepository.findFirstByPriceInCentsAndTargetAudience(0, UserType.OWNER))
            .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            subscriptionService.createInitialFreeSubscription(user);
        });
        
        assertEquals("Plano Grátis padrão não configurado para o perfil OWNER", ex.getMessage());
    }
}