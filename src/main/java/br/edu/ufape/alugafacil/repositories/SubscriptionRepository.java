package br.edu.ufape.alugafacil.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.alugafacil.enums.SubscriptionStatus;
import br.edu.ufape.alugafacil.models.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID>{
        Optional<Subscription> findFirstByUserUserIdAndStatus(UUID userId, SubscriptionStatus status);  
}
