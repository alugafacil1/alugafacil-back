package br.edu.ufape.alugafacil.repositories;

import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.models.enums.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
    List<Plan> findByPlanType(PlanType planType);

    List<Plan> findByPriceInCentsLessThanEqual(Integer maxPrice);

    boolean existsByName(String name);
}
