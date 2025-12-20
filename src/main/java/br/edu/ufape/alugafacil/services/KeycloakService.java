package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.dtos.auth.TokenResponse;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.exceptions.KeycloakAuthenticationException;
import br.edu.ufape.alugafacil.services.interfaces.IKeycloakService;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloakService implements IKeycloakService {

    private Keycloak keycloak;

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @PostConstruct
    @Override
    public void init() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm("master")
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    @Override
    public TokenResponse login(String email, String password) throws KeycloakAuthenticationException {
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", email);
        formData.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, TokenResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                TokenResponse tokenResponse = response.getBody();
                
                try {
                    String userId = getUserId(email);
                    List<RoleRepresentation> roles = keycloak.realm(realm).users().get(userId).roles().realmLevel().listEffective();
                    tokenResponse.setRoles(roles.stream().map(RoleRepresentation::getName).toList());
                } catch (Exception e) {
                    log.warn("Login realizado para [{}], mas falha ao recuperar roles: {}", email, e.getMessage());
                }
                
                return tokenResponse;
            }

            throw new KeycloakAuthenticationException("Erro ao autenticar no Keycloak: " + response.getStatusCode());

        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.warn("Tentativa de login falhou para [{}]: Credenciais inválidas", email);
                throw new KeycloakAuthenticationException("Credenciais inválidas. Verifique o email e a senha.");
            }
            log.error("Erro HTTP ao conectar no Keycloak: {}", e.getStatusCode(), e);
            throw new KeycloakAuthenticationException("Erro ao autenticar no Keycloak: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            log.error("Servidor Keycloak inacessível: {}", e.getMessage());
            throw new KeycloakAuthenticationException("Não foi possível acessar o servidor Keycloak. Verifique sua conexão.", e);
        } catch (Exception e) {
            log.error("Erro inesperado no login: {}", e.getMessage(), e);
            throw new KeycloakAuthenticationException("Erro inesperado durante o login.", e);
        }
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        String keycloakTokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(keycloakTokenUrl, request, TokenResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao atualizar token: {}", e.getMessage());
            throw new KeycloakAuthenticationException("Falha ao atualizar o token: " + e.getMessage());
        }
    }

    @Override
    public void createUser(String username, String email, String password, UserType userType) throws KeycloakAuthenticationException {
        // 1. Extrai o nome da role do Enum
        String role = userType.name(); 

        try {
            log.info("Tentando criar usuário: {} com role: {}", email, role);

            // --- PREPARAÇÃO DA SENHA (CREDENTIAL) ---
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false); // Importante: False para não pedir troca no login
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);

            // --- DEFINIÇÃO DO USUÁRIO ---
            UserRepresentation user = new UserRepresentation();
            user.setUsername(email); // Username igual ao email
            user.setEmail(email);
            user.setFirstName(username);
            
            // CORREÇÃO DO LAST NAME: Passar null se não tiver
            user.setLastName(null); 
            
            user.setEnabled(true);
            user.setEmailVerified(true); // Força email verificado
            
            // CORREÇÃO PRINCIPAL: Envia a senha JUNTO com a criação
            user.setCredentials(Collections.singletonList(credential));
            
            // Garante que não haja ações requeridas (como verificar email ou mudar senha)
            user.setRequiredActions(Collections.emptyList());

            // --- CHAMADA AO KEYCLOAK ---
            Response response = keycloak.realm(realm).users().create(user);

            if (response.getStatus() != 201) {
                if (response.getStatus() == 409) {
                    throw new KeycloakAuthenticationException("Usuário ou Email já cadastrado.");
                }
                // Se der erro, loga o corpo da resposta para sabermos o motivo real
                String errorBody = response.readEntity(String.class);
                log.error("Erro criando usuário no Keycloak. Status: {} | Body: {}", response.getStatus(), errorBody);
                throw new KeycloakAuthenticationException("Erro ao criar usuário: " + response.getStatus());
            }

            // --- ATRIBUIÇÃO DE ROLE ---
            // Precisamos do ID para adicionar a role. 
            // Como acabamos de criar, buscamos pelo email.
            String userId = getUserId(email);
            
            try {
                RoleRepresentation userRole = keycloak.realm(realm).roles().get(role).toRepresentation();
                keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(userRole));
            } catch (NotFoundException e) {
                log.error("Role '{}' não encontrada. Removendo usuário para consistência.", role);
                deleteUser(userId); 
                throw new KeycloakAuthenticationException("Role '" + role + "' não encontrada no Keycloak.");
            }
            
            log.info("Usuário criado, senha definida e role atribuída: {}", email);

        } catch (KeycloakAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro crítico ao criar usuário", e);
            throw new KeycloakAuthenticationException("Erro inesperado ao criar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateUser(String userId, String email) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
            user.setEmail(email);
            user.setEmailVerified(true);
            keycloak.realm(realm).users().get(userId).update(user);
            log.info("Usuário atualizado: {}", userId);
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário {}", userId, e);
            throw new KeycloakAuthenticationException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String userId) {
        try {
            keycloak.realm(realm).users().get(userId).remove();
            log.info("Usuário deletado: {}", userId);
        } catch (Exception e) {
            log.error("Erro ao deletar usuário {}", userId, e);
            throw new KeycloakAuthenticationException("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    @Override
    public String getUserId(String username) {
        List<UserRepresentation> users = keycloak.realm(realm).users().search(username, true);
        
        if (users.isEmpty()) {
             users = keycloak.realm(realm).users().search(null, null, null, username, null, null);
        }

        if (!users.isEmpty()) {
            return users.get(0).getId();
        }
        
        throw new KeycloakAuthenticationException("Usuário não encontrado: " + username);
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        try {
            List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, null, null);

            if (users.isEmpty()) {
                log.warn("Solicitação de reset de senha para email inexistente: {}", email);
                return; 
            }

            UserRepresentation user = users.get(0);
            
            keycloak.realm(realm).users().get(user.getId())
                    .executeActionsEmail(Collections.singletonList("UPDATE_PASSWORD"));
            
            log.info("Email de reset de senha enviado para: {}", email);

        } catch (Exception e) {
            log.error("Erro ao enviar email de reset de senha: {}", e.getMessage());
            throw new KeycloakAuthenticationException("Erro ao processar solicitação de senha.");
        }
    }
}