package br.edu.ufape.alugafacil.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.alugafacil.dtos.realStateAgency.MemberResponse;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyResponse;
import br.edu.ufape.alugafacil.dtos.realStateAgency.TransferRequest;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.services.RealStateAgencyService;


@RestController
@RequestMapping("api/realStateAgencies")
@CrossOrigin(origins = "*")
public class RealStateAgencyController {
    
    @Autowired
    private RealStateAgencyService realStateAgencyService;

    @GetMapping
    public ResponseEntity<List<RealStateAgencyResponse>> getAllRealStateAgencies() {
        try {
            List<RealStateAgency> realStateAgencies = realStateAgencyService.getAllRealStateAgencies();

            List<RealStateAgencyResponse> responses = realStateAgencies.stream()
                    .map(this::convertToResponse)
                    .toList();

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<RealStateAgencyResponse> getRealStateAgencyById(@PathVariable UUID id) {
        try {
            RealStateAgency realStateAgency = realStateAgencyService.getRealStateAgencyById(id);

            RealStateAgencyResponse response = convertToResponse(realStateAgency);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<RealStateAgencyResponse> createRealStateAgency(@RequestBody RealStateAgencyRequest realStateAgencyRequest) {
        try {
            RealStateAgency realStateAgency = realStateAgencyService.createRealStateAgency(realStateAgencyRequest);

            RealStateAgencyResponse response = convertToResponse(realStateAgency);

            return ResponseEntity.ok().body(response);
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

    @PutMapping("/{id}")
    public ResponseEntity<RealStateAgencyResponse> updateRealStateAgency(@PathVariable UUID id, @RequestBody RealStateAgencyRequest realStateAgencyRequest) {
        try {
            RealStateAgency realStateAgency = realStateAgencyService.updateRealStateAgency(id, realStateAgencyRequest);

            RealStateAgencyResponse response = convertToResponse(realStateAgency);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{agencyId}/members")
    public ResponseEntity<List<MemberResponse>> getMembers(@PathVariable UUID agencyId) {
        try {
            List<User> users = realStateAgencyService.getMembers(agencyId);
            List<MemberResponse> responses = users.stream().map(u -> {
                MemberResponse mr = new MemberResponse();
                mr.setUserId(u.getUserId());
                mr.setName(u.getName());
                mr.setEmail(u.getEmail());
                mr.setUserType(u.getUserType());
                return mr;
            }).toList();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{agencyId}/members/{userId}")
    public ResponseEntity<Void> addMember(@PathVariable UUID agencyId, @PathVariable UUID userId, @RequestHeader("X-User-Id") UUID actingUserId) {
        try {
            realStateAgencyService.addMember(agencyId, userId, actingUserId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{agencyId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable UUID agencyId, @PathVariable UUID userId, @RequestHeader("X-User-Id") UUID actingUserId) {
        try {
            realStateAgencyService.removeMember(agencyId, userId, actingUserId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{agencyId}/properties/{propertyId}/transfer")
    public ResponseEntity<Void> transferProperty(@PathVariable UUID agencyId, @PathVariable UUID propertyId, @RequestBody TransferRequest transferRequest, @RequestHeader("X-User-Id") UUID actingUserId) {
        try {
            realStateAgencyService.transferProperty(agencyId, propertyId, transferRequest.getTargetUserId(), actingUserId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private RealStateAgencyResponse convertToResponse(RealStateAgency realStateAgency) {
        RealStateAgencyResponse response = new RealStateAgencyResponse();

        response.setAgencyId(realStateAgency.getAgencyId());
        response.setName(realStateAgency.getName());
        response.setCorporateName(realStateAgency.getCorporateName());
        response.setEmail(realStateAgency.getEmail());
        response.setPhotoUrl(realStateAgency.getPhotoUrl());
        response.setCnpj(realStateAgency.getCnpj());
        response.setWebsite(realStateAgency.getWebsite());
        response.setPhoneNumber(realStateAgency.getPhoneNumber());

        return response;
    }
}
