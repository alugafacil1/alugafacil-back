package br.edu.ufape.alugafacil.services.interfaces;

import br.edu.ufape.alugafacil.dtos.auth.TokenResponse;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.exceptions.KeycloakAuthenticationException;
import jakarta.annotation.PostConstruct;

public interface IKeycloakService {
    @PostConstruct
    void init();

    TokenResponse login(String email, String password) throws KeycloakAuthenticationException;
    TokenResponse refreshToken(String refreshToken);
    void createUser(String username, String email, String password, UserType userType) throws KeycloakAuthenticationException;
    void updateUser(String userId, String email);
    void deleteUser(String userId);
    String getUserId(String username);
    void sendResetPasswordEmail(String email);
}
