package br.edu.ufape.alugafacil.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionRequestDTO;
import br.edu.ufape.alugafacil.dtos.subscription.SubscriptionResponseDTO;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.services.interfaces.ISubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final ISubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> subscribe(
            // @AuthenticationPrincipal 
            User user,
            @RequestBody @Valid SubscriptionRequestDTO request) {
        
        var response = subscriptionService.subscribe(user.getUserId(), request);
        
        return ResponseEntity.ok(response);
    }
}
