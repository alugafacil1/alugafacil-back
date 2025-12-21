package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.dtos.PlanRequestDTO;
import br.edu.ufape.alugafacil.dtos.PlanResponseDTO;
import br.edu.ufape.alugafacil.exceptions.DuplicatePlanNameException;
import br.edu.ufape.alugafacil.exceptions.InvalidPlanTypeException;
import br.edu.ufape.alugafacil.exceptions.PlanNotFoundException;
import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.repositories.PlanRepository;
import br.edu.ufape.alugafacil.repositories.SubscriptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional
    public PlanResponseDTO createPlan(PlanRequestDTO planRequestDTO) {
   
        validateTargetAudience(planRequestDTO.getTargetAudience());

        if (planRepository.existsByName(planRequestDTO.getName())) {
            throw new DuplicatePlanNameException(planRequestDTO.getName());
        }

        Plan plan = new Plan();
        plan.setName(planRequestDTO.getName());
        plan.setPriceInCents(planRequestDTO.getPriceInCents());
        plan.setHasVideo(planRequestDTO.getHasVideo());
        plan.setImagesCount(planRequestDTO.getImagesCount());
        plan.setPropertiesCount(planRequestDTO.getPropertiesCount());
        plan.setIsPriority(planRequestDTO.getIsPriority());
        plan.setHasNotification(planRequestDTO.getHasNotification());
        
        plan.setTargetAudience(planRequestDTO.getTargetAudience());

        Plan savedPlan = planRepository.save(plan);
        return new PlanResponseDTO(savedPlan);
    }

    @Transactional(readOnly = true)
    public PlanResponseDTO getPlanById(UUID planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        return new PlanResponseDTO(plan);
    }

    @Transactional(readOnly = true)
    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(PlanResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlanResponseDTO> getPlansByTargetAudience(UserType targetAudience) {
        return planRepository.findByTargetAudience(targetAudience).stream()
                .map(PlanResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlanResponseDTO> getPlansByMaxPrice(Integer maxPrice) {
        return planRepository.findByPriceInCentsLessThanEqual(maxPrice).stream()
                .map(PlanResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlanResponseDTO updatePlan(UUID planId, PlanRequestDTO planRequestDTO) {
        
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        validateTargetAudience(planRequestDTO.getTargetAudience());

        if (!plan.getName().equals(planRequestDTO.getName())
                && planRepository.existsByName(planRequestDTO.getName())) {
            throw new DuplicatePlanNameException(planRequestDTO.getName());
        }

        plan.setName(planRequestDTO.getName());
        plan.setPriceInCents(planRequestDTO.getPriceInCents());
        plan.setHasVideo(planRequestDTO.getHasVideo());
        plan.setImagesCount(planRequestDTO.getImagesCount());
        plan.setPropertiesCount(planRequestDTO.getPropertiesCount());
        plan.setIsPriority(planRequestDTO.getIsPriority());
        plan.setHasNotification(planRequestDTO.getHasNotification());
        
        plan.setTargetAudience(planRequestDTO.getTargetAudience());

        Plan updatedPlan = planRepository.save(plan);
        return new PlanResponseDTO(updatedPlan);
    }

    @Transactional
    public void deletePlan(UUID planId) {
        if (!planRepository.existsById(planId)) {
            throw new PlanNotFoundException(planId);
        }
        planRepository.deleteById(planId);
    }

    private void validateTargetAudience(UserType targetAudience) {
        if (targetAudience == null) {
            throw new InvalidPlanTypeException();
        }
    }
}