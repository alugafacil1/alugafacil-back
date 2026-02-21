package br.edu.ufape.alugafacil.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.edu.ufape.alugafacil.services.NotificationService;
import br.edu.ufape.alugafacil.services.interfaces.IFileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.services.SubscriptionService;
import br.edu.ufape.alugafacil.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private IFileStorageService fileStorageService;

    @MockBean
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve persistir usuário no banco H2 e recuperar pelo ID")
    void shouldPersistAndRetrieveUser() {
        UserRequest request = criarUserRequest("Maria Teste", "maria@test.com", "11111111111");

        UserResponse saved = userService.saveUser(request);

        assertNotNull(saved);
        assertNotNull(saved.userId());

        UserResponse found = userService.getUserById(saved.userId());

        assertEquals("Maria Teste", found.name());
        assertEquals("maria@test.com", found.email());
        assertEquals(UserType.TENANT, found.userType());
    }

    @Test
    @DisplayName("Deve listar todos os usuários cadastrados")
    void shouldReturnAllUsers() {
        userService.saveUser(criarUserRequest("User 1", "u1@test.com", "111"));
        userService.saveUser(criarUserRequest("User 2", "u2@test.com", "222"));

        Page<UserResponse> page = userService.getAllUsers(PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Deve atualizar dados do usuário existente")
    void shouldUpdateUserSuccessfully() {
        UserResponse saved = userService.saveUser(
                criarUserRequest("Antigo Nome", "old@test.com", "333")
        );

        UserRequest updateRequest = criarUserRequest(
                "Novo Nome",
                "new@test.com",
                "333"
        );

        UserResponse updated = userService.updateUser(saved.userId(), updateRequest);

        assertEquals("Novo Nome", updated.name());
        assertEquals("new@test.com", updated.email());
    }

    @Test
    @DisplayName("Deve deletar usuário existente")
    void shouldDeleteUserSuccessfully() {
        UserResponse saved = userService.saveUser(
                criarUserRequest("Delete Me", "delete@test.com", "444")
        );

        userService.deleteUser(saved.userId());

        assertTrue(userRepository.findById(saved.userId()).isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar token FCM do usuário")
    void shouldUpdateFcmToken() {
        UserResponse saved = userService.saveUser(
                criarUserRequest("FCM User", "fcm@test.com", "555")
        );

        userService.updateFcmToken(saved.userId(), "fake-fcm-token");

        User user = userRepository.findById(saved.userId()).orElseThrow();
        assertEquals("fake-fcm-token", user.getFcmToken());
    }

    private UserRequest criarUserRequest(String nome, String email, String cpf) {
        return new UserRequest(
                nome,
                email,
                null,
                cpf,
                null,
                "123456",
                "81999999999",
                UserType.TENANT,
                null,
                null,
                null,
                null
        );
    }
}

