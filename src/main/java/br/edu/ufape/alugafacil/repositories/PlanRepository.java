package br.edu.ufape.alugafacil.repositories;

import br.edu.ufape.alugafacil.models.Plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.edu.ufape.alugafacil.enums.UserType;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByTargetAudience(UserType targetAudience);
    List<Plan> findByPriceInCentsLessThanEqual(Integer maxPrice);
    boolean existsByName(String name);
    Optional<Plan> findFirstByPriceInCentsAndTargetAudience(Integer price, UserType targetAudience);
}
