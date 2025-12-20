package br.edu.ufape.alugafacil.controllers;

import br.edu.ufape.alugafacil.dtos.PlanRequestDTO;
import br.edu.ufape.alugafacil.dtos.PlanResponseDTO;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.services.PlanService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PostMapping
    public ResponseEntity<PlanResponseDTO> createPlan(@Valid @RequestBody PlanRequestDTO planRequestDTO) {
        PlanResponseDTO response = planService.createPlan(planRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<PlanResponseDTO> getPlanById(@PathVariable UUID planId) {
        PlanResponseDTO response = planService.getPlanById(planId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PlanResponseDTO>> getAllPlans(
            @RequestParam(required = false) UserType targetAudience, 
            @RequestParam(required = false) Integer maxPrice) {

        List<PlanResponseDTO> response;

        if (targetAudience != null) {
            response = planService.getPlansByTargetAudience(targetAudience);
        } else if (maxPrice != null) {
            response = planService.getPlansByMaxPrice(maxPrice);
        } else {
            response = planService.getAllPlans();
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<PlanResponseDTO> updatePlan(
            @PathVariable UUID planId,
            @Valid @RequestBody PlanRequestDTO planRequestDTO) {
        PlanResponseDTO response = planService.updatePlan(planId, planRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable UUID planId) {
        planService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }
}