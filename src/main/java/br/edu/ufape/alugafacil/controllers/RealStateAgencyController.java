package br.edu.ufape.alugafacil.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyResponse;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.services.RealStateAgencyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/realStateAgencies")
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
    public ResponseEntity<RealStateAgencyResponse> getRealStateAgencyById(@PathVariable String id) {
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
    public ResponseEntity<Void> deleteRealStateAgency(@PathVariable String id) {
        try {
            realStateAgencyService.deleteRealStateAgency(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RealStateAgencyResponse> updateRealStateAgency(@PathVariable String id, @RequestBody RealStateAgencyRequest realStateAgencyRequest) {
        try {
            RealStateAgency realStateAgency = realStateAgencyService.updateRealStateAgency(id, realStateAgencyRequest);

            RealStateAgencyResponse response = convertToResponse(realStateAgency);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private RealStateAgencyResponse convertToResponse(RealStateAgency realStateAgency) {
        RealStateAgencyResponse response = new RealStateAgencyResponse();

        response.setAgencyId(realStateAgency.getAgencyId().toString());
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
