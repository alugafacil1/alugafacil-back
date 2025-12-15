package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.dtos.PlanRequestDTO;
import br.edu.ufape.alugafacil.dtos.PlanResponseDTO;
import br.edu.ufape.alugafacil.exceptions.DuplicatePlanNameException;
import br.edu.ufape.alugafacil.exceptions.InvalidPlanTypeException;
import br.edu.ufape.alugafacil.exceptions.PlanNotFoundException;
import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.models.enums.PlanType;
import br.edu.ufape.alugafacil.repositories.PlanRepository;

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

    @Transactional
    public PlanResponseDTO createPlan(PlanRequestDTO planRequestDTO) {
   
        validatePlanType(planRequestDTO.getPlanType());

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
        plan.setPlanType(planRequestDTO.getPlanType());

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
    public List<PlanResponseDTO> getPlansByType(PlanType planType) {
        return planRepository.findByPlanType(planType).stream()
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

        validatePlanType(planRequestDTO.getPlanType());

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
        plan.setPlanType(planRequestDTO.getPlanType());

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

    /**
     * Valida se o tipo de plano é válido
     * 
     * @param planType o tipo de plano a ser validado
     * @throws InvalidPlanTypeException se o tipo for null ou inválido
     */
    private void validatePlanType(PlanType planType) {
        if (planType == null) {
            throw new InvalidPlanTypeException();
        }

        // Verificar se o valor do enum é válido
        boolean isValid = false;
        for (PlanType validType : PlanType.values()) {
            if (validType.equals(planType)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new InvalidPlanTypeException(planType.toString());
        }
    }
}
