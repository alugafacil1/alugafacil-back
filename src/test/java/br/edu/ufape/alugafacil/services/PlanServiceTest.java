package br.edu.ufape.alugafacil.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.ufape.alugafacil.dtos.PlanRequestDTO;
import br.edu.ufape.alugafacil.dtos.PlanResponseDTO;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.exceptions.DuplicatePlanNameException;
import br.edu.ufape.alugafacil.exceptions.InvalidPlanTypeException;
import br.edu.ufape.alugafacil.exceptions.PlanNotFoundException;
import br.edu.ufape.alugafacil.models.Plan;
import br.edu.ufape.alugafacil.repositories.PlanRepository;
import br.edu.ufape.alugafacil.repositories.SubscriptionRepository;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private PlanService planService;

    private PlanRequestDTO planRequestDTO;
    private Plan planEntity;
    private UUID planId;

    @BeforeEach
    void setUp() {
        planId = UUID.randomUUID();

        planRequestDTO = new PlanRequestDTO(
            "Plano Ouro",
            2990,
            true,
            10,
            5,
            true,
            true,
            UserType.OWNER
        );

        planEntity = new Plan();
        planEntity.setPlanId(planId);
        planEntity.setName("Plano Ouro");
        planEntity.setPriceInCents(2990);
        planEntity.setTargetAudience(UserType.OWNER);
    }

    @Test
    @DisplayName("Deve criar plano com sucesso (Happy Path)")
    void shouldCreatePlanSuccessfully() {
        when(planRepository.existsByName(anyString())).thenReturn(false);
        when(planRepository.save(any(Plan.class))).thenReturn(planEntity);

        PlanResponseDTO response = planService.createPlan(planRequestDTO);

        assertNotNull(response);
        assertEquals(planEntity.getName(), response.getName());
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao criar plano com nome duplicado")
    void shouldThrowDuplicateNameExceptionOnCreate() {
        when(planRepository.existsByName(planRequestDTO.getName())).thenReturn(true);

        assertThrows(DuplicatePlanNameException.class, () -> {
            planService.createPlan(planRequestDTO);
        });
        
        verify(planRepository, never()).save(any(Plan.class));
    }

    @Test
    @DisplayName("Deve lançar erro se o público alvo for nulo")
    void shouldThrowInvalidPlanTypeExceptionWhenTargetAudienceIsNull() {
        planRequestDTO.setTargetAudience(null);

        assertThrows(InvalidPlanTypeException.class, () -> {
            planService.createPlan(planRequestDTO);
        });
    }

    @Test
    @DisplayName("Deve retornar plano por ID existente")
    void shouldReturnPlanById() {
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));

        PlanResponseDTO response = planService.getPlanById(planId);

        assertNotNull(response);
        assertEquals(planId, response.getPlanId());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar ID inexistente")
    void shouldThrowNotFoundWhenIdDoesNotExist() {
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        assertThrows(PlanNotFoundException.class, () -> {
            planService.getPlanById(planId);
        });
    }

    @Test
    @DisplayName("Deve retornar lista de planos filtrada por público alvo")
    void shouldReturnPlansByTargetAudience() {
        when(planRepository.findByTargetAudience(UserType.OWNER))
            .thenReturn(List.of(planEntity));

        List<PlanResponseDTO> list = planService.getPlansByTargetAudience(UserType.OWNER);

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(UserType.OWNER, list.get(0).getTargetAudience());
    }

    @Test
    @DisplayName("Deve atualizar plano com sucesso")
    void shouldUpdatePlanSuccessfully() {
        when(planRepository.findById(planId)).thenReturn(Optional.of(planEntity));
        when(planRepository.save(any(Plan.class))).thenReturn(planEntity);

        PlanResponseDTO response = planService.updatePlan(planId, planRequestDTO);

        assertNotNull(response);
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar para um nome que já existe em outro plano")
    void shouldThrowDuplicateNameOnUpdate() {
        Plan existingPlan = new Plan();
        existingPlan.setPlanId(planId);
        existingPlan.setName("Nome Antigo");

        when(planRepository.findById(planId)).thenReturn(Optional.of(existingPlan));
        when(planRepository.existsByName("Plano Ouro")).thenReturn(true);

        assertThrows(DuplicatePlanNameException.class, () -> {
            planService.updatePlan(planId, planRequestDTO);
        });
    }

    @Test
    @DisplayName("Deve deletar plano existente")
    void shouldDeletePlanSuccessfully() {
        when(planRepository.existsById(planId)).thenReturn(true);

        planService.deletePlan(planId);

        verify(planRepository, times(1)).deleteById(planId);
    }

    @Test
    @DisplayName("Deve lançar erro ao deletar plano inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentPlan() {
        when(planRepository.existsById(planId)).thenReturn(false);

        assertThrows(PlanNotFoundException.class, () -> {
            planService.deletePlan(planId);
        });
        
        verify(planRepository, never()).deleteById(any());
    }
}