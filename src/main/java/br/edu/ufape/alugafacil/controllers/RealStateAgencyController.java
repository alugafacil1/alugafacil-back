package br.edu.ufape.alugafacil.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyResponse;
import br.edu.ufape.alugafacil.dtos.realStateAgency.TransferRequest;
import br.edu.ufape.alugafacil.services.RealStateAgencyService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/realStateAgencies")
@RequiredArgsConstructor
public class RealStateAgencyController {
    
    private final RealStateAgencyService realStateAgencyService;

    @GetMapping
    public ResponseEntity<Page<RealStateAgencyResponse>> getAllRealStateAgencies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<RealStateAgencyResponse> responses = realStateAgencyService.getAllRealStateAgencies(pageable);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealStateAgencyResponse> getRealStateAgencyById(@PathVariable UUID id) {
        try {
            RealStateAgencyResponse response = realStateAgencyService.getRealStateAgencyById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<RealStateAgencyResponse> createRealStateAgency(@RequestBody RealStateAgencyRequest realStateAgencyRequest) {
        try {
            RealStateAgencyResponse response = realStateAgencyService.createRealStateAgency(realStateAgencyRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RealStateAgencyResponse> updateRealStateAgency(@PathVariable UUID id, @RequestBody RealStateAgencyRequest realStateAgencyRequest) {
        try {
            RealStateAgencyResponse response = realStateAgencyService.updateRealStateAgency(id, realStateAgencyRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRealStateAgency(@PathVariable UUID id) {
        try {
            realStateAgencyService.deleteRealStateAgency(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{agencyId}/members/{userId}")
    public ResponseEntity<Void> addMember(
            @PathVariable UUID agencyId, 
            @PathVariable UUID userId, 
            @RequestHeader("X-User-Id") UUID actingUserId) {
        try {
            realStateAgencyService.addMember(agencyId, userId, actingUserId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{agencyId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID agencyId, 
            @PathVariable UUID userId, 
            @RequestHeader("X-User-Id") UUID actingUserId) {
        try {
            realStateAgencyService.removeMember(agencyId, userId, actingUserId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{agencyId}/properties/{propertyId}/transfer")
    public ResponseEntity<Void> transferProperty(
            @PathVariable UUID agencyId, 
            @PathVariable UUID propertyId, 
            @RequestBody TransferRequest transferRequest, 
            @RequestHeader("X-User-Id") UUID actingUserId) {
        try {
            realStateAgencyService.transferProperty(agencyId, propertyId, transferRequest.getTargetUserId(), actingUserId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}