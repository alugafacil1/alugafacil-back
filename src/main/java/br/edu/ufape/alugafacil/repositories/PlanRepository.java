package br.edu.ufape.alugafacil.repositories;

import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.models.enums.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByPlanType(PlanType planType);

    List<Plan> findByPriceInCentsLessThanEqual(Integer maxPrice);

    boolean existsByName(String name);
}
