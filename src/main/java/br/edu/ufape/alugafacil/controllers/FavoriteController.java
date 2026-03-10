package br.edu.ufape.alugafacil.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.alugafacil.dtos.property.PropertyResponse;
import br.edu.ufape.alugafacil.mappers.PropertyMapper;
import br.edu.ufape.alugafacil.models.Favorite;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.repositories.FavoriteRepository;
import br.edu.ufape.alugafacil.services.FavoriteService;
import br.edu.ufape.alugafacil.services.interfaces.IPropertyService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final PropertyMapper propertyMapper;


    // Endpoint: POST /api/favorites/toggle?userId=123&propertyId=456
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Boolean>> toggleFavorite(
            @RequestParam UUID userId, 
            @RequestParam UUID propertyId) {
        
        boolean isFavorited = favoriteService.toggleFavorite(userId, propertyId);
        
        // Retorna um JSON tipo: { "isFavorited": true }
        return ResponseEntity.ok(Map.of("isFavorited", isFavorited));
    }

    // Endpoint: GET /api/favorites/check?userId=...&propertyId=...
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkIfFavorited(
            @RequestParam UUID userId, 
            @RequestParam UUID propertyId) {
        
        // Agora delegamos para o Service fazer a consulta no banco
        boolean isFavorited = favoriteService.checkIfFavorited(userId, propertyId);
        
        return ResponseEntity.ok(Map.of("isFavorited", isFavorited));
    }

    // Endpoint: GET /api/favorites/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PropertyResponse>> getUserFavorites(@PathVariable UUID userId) {
        
        List<Favorite> favorites = favoriteService.getUserFavorites(userId);
        
        List<PropertyResponse> responseList = favorites.stream()
            .map(favorite -> {
                Property property = favorite.getProperty();
                
                // 1. Extraímos para uma variável tipada. 
                // Se der erro de linha vermelha AQUI, passe o mouse em cima para ver o que o toResponse exige!
                // Se ele pedir Long, e você passar null, faça um cast: (Long) null
                PropertyResponse response = propertyMapper.toResponse(property, null); 
                
                return response;
            })
            .toList();

        return ResponseEntity.ok(responseList);
    }
}