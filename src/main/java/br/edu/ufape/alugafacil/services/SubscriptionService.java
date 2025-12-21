package br.edu.ufape.alugafacil.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionRequestDTO;
import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionResponseDTO;
import br.edu.ufape.alugafacil.enums.PaymentStatus;
import br.edu.ufape.alugafacil.enums.SubscriptionStatus;
import br.edu.ufape.alugafacil.mappers.SubscriptionMapper;
import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.models.Subscription;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.PlanRepository;
import br.edu.ufape.alugafacil.repositories.SubscriptionRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.services.interfaces.ISubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Transactional
    public SubscriptionResponseDTO subscribe(UUID userId, SubscriptionRequestDTO request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Plan newPlan = planRepository.findById(request.planId())
            .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        
        if (newPlan.getTargetAudience() != user.getUserType()) {
            throw new RuntimeException("Este plano não está disponível para o seu tipo de perfil (" + user.getUserType() + ")");
        }

        var activeSub = subscriptionRepository.findFirstByUserUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        if (activeSub.isPresent()) {
            Subscription oldSub = activeSub.get();

            if (oldSub.getPlan().getPlanId().equals(newPlan.getPlanId())) {
                throw new RuntimeException("Você já possui este plano ativo.");
            }
            oldSub.setStatus(PaymentStatus.CANCELLED);
            subscriptionRepository.save(oldSub);
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(newPlan);
        subscription.setStartDate(LocalDate.now());
        subscription.setNextBillingDate(LocalDate.now().plusDays(30));

        subscription.setStatus(PaymentStatus.ACTIVE); 

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toResponse(savedSubscription);
    }

    @Transactional
    public void createInitialFreeSubscription(User user) {
        Plan freePlan = planRepository.findFirstByPriceInCentsAndTargetAudience(0, user.getUserType())
             .orElseThrow(() -> new RuntimeException("Plano Grátis padrão não configurado para o perfil " + user.getUserType()));
        
        Subscription sub = new Subscription();
        sub.setUser(user);
        sub.setPlan(freePlan);
        sub.setStartDate(LocalDate.now());
        sub.setStatus(PaymentStatus.ACTIVE); 
        
        subscriptionRepository.save(sub);
    }
}