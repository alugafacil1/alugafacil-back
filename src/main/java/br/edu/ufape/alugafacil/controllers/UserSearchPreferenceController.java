package br.edu.ufape.alugafacil.controllers;


import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceRequest;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceResponse;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.exceptions.UserPreferenceNotFound;
import br.edu.ufape.alugafacil.services.interfaces.IUserPreferenceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/preferences")
@AllArgsConstructor
public class UserSearchPreferenceController {

    private IUserPreferenceService userPreferenceService;


    @PostMapping
    public ResponseEntity<?> createPreference(@RequestBody UserPreferenceRequest userPreferenceRequest) {

        try {
            UserPreferenceResponse preferenceResponse = userPreferenceService.save(userPreferenceRequest);
            return  ResponseEntity.ok(preferenceResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllPreferences(UUID idUser) {
        try {
            List<UserPreferenceResponse> preferences = userPreferenceService.getAllPreference(idUser);
            return ResponseEntity.ok(preferences);
        } catch (UserNotFoundException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPreferenceById(@PathVariable UUID id) {
        try {
            UserPreferenceResponse userPreferenceResponse = userPreferenceService.getPreferenceById(id);
            return ResponseEntity.ok(userPreferenceResponse);
        } catch (UserPreferenceNotFound e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePreference(@PathVariable UUID id, @RequestBody UserPreferenceRequest preferenceRequest) {
        try {
            UserPreferenceResponse updated = userPreferenceService.updatePreference(id, preferenceRequest);
            return ResponseEntity.ok(updated);
        } catch (UserPreferenceNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePreference(@PathVariable UUID id) {
        try {
            userPreferenceService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (UserPreferenceNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
