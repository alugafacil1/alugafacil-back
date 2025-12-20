package br.edu.ufape.alugafacil.controllers;

import br.edu.ufape.alugafacil.dtos.auth.*;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.services.interfaces.IKeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IKeycloakService keycloakService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse token = keycloakService.login(request.email(), request.password());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        UserType userType = mapFrontendTypeToEnum(request.type());

        if (userType == UserType.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            keycloakService.createUser(request.email(), request.email(), request.password(), userType);
        } catch (Exception e) {
            throw e;
        }

        try {
            User newUser = User.builder()
                    .name(request.name())
                    .email(request.email())
                    .phoneNumber(request.phone())
                    .cpf(request.cpf())
                    .userType(userType)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(newUser);
        } catch (Exception e) {
            String keycloakId = keycloakService.getUserId(request.email());
            keycloakService.deleteUser(keycloakId);
            throw new RuntimeException("Erro ao salvar dados locais. Cadastro cancelado.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse token = keycloakService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody EmailRequest emailRequest) {
        keycloakService.sendResetPasswordEmail(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    private UserType mapFrontendTypeToEnum(String frontendType) {
        if (frontendType == null) throw new IllegalArgumentException("Tipo de usuário é obrigatório.");

        return switch (frontendType.toLowerCase().trim()) {
            case "locatario", "locatário", "tenant" -> UserType.TENANT;
            case "proprietario", "proprietário", "owner" -> UserType.OWNER;
            case "corretor", "realtor" -> UserType.REALTOR;
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + frontendType);
        };
    }
}