package br.edu.ufape.alugafacil.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.exceptions.ResourceNotFoundException;
import br.edu.ufape.alugafacil.exceptions.UserCpfDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserEmailDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.mappers.RealStateAgencyPropertyMapper;
import br.edu.ufape.alugafacil.mappers.UserPropertyMapper;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPropertyMapper userPropertyMapper;

    @Mock
    private RealStateAgencyPropertyMapper agencyMapper;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User();
        user.setUserId(userId);
        user.setCpf("12345678900");
        user.setEmail("user@email.com");

        userRequest = new UserRequest(
                "Maria Silva",
                "user@email.com",
                null,
                "12345678900",
                null,
                "senha123",
                "81999999999",
                UserType.TENANT,
                null,
                null,
                null,
                null
        );

        // Usando mock para o Response evita quebrar o teste se o Record mudar no futuro
        userResponse = mock(UserResponse.class);
        lenient().when(userResponse.userId()).thenReturn(userId);
        lenient().when(userResponse.name()).thenReturn("Maria Silva");
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso (Happy Path)")
    void shouldSaveUserSuccessfully() {
        // Mocks necessários para passar nas validações de duplicidade
        when(userRepository.findUserByCpf(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(userPropertyMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userPropertyMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.saveUser(userRequest);

        assertNotNull(response);
        assertEquals(userResponse.name(), response.name());
        verify(subscriptionService, times(1))
                .createInitialFreeSubscription(user);
    }

    @Test
    @DisplayName("Deve lançar erro ao salvar usuário com CPF duplicado")
    void shouldThrowCpfDuplicatedException() {
        when(userRepository.findUserByCpf(anyString()))
                .thenReturn(Optional.of(user));

        assertThrows(UserCpfDuplicadoException.class, () ->
                userService.saveUser(userRequest)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro ao salvar usuário com email duplicado")
    void shouldThrowEmailDuplicatedException() {
        when(userRepository.findUserByCpf(anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        assertThrows(UserEmailDuplicadoException.class, () ->
                userService.saveUser(userRequest)
        );
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void shouldUpdateUserSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findUserByCpf(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userPropertyMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.updateUser(userId, userRequest);

        assertNotNull(response);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar usuário inexistente")
    void shouldThrowUserNotFoundOnUpdate() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.updateUser(userId, userRequest)
        );
    }

    @Test
    @DisplayName("Deve retornar todos os usuários")
    void shouldReturnAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(userPropertyMapper.toResponse(user)).thenReturn(userResponse);

        Page<UserResponse> page = userService.getAllUsers(pageable);

        assertEquals(1, page.getContent().size());
    }

    @Test
    @DisplayName("Deve retornar usuário por ID")
    void shouldReturnUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userPropertyMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.getUserById(userId);

        assertEquals(userId, response.userId());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar usuário inexistente")
    void shouldThrowUserNotFoundOnGetById() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserById(userId)
        );
    }

    @Test
    @DisplayName("Deve deletar usuário existente")
    void shouldDeleteUserSuccessfully() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Deve lançar erro ao deletar usuário inexistente")
    void shouldThrowUserNotFoundOnDelete() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                userService.deleteUser(userId)
        );
    }

    @Test
    @DisplayName("Deve atualizar FCM Token com sucesso")
    void shouldUpdateFcmTokenSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updateFcmToken(userId, "fake-token");

        assertEquals("fake-token", user.getFcmToken());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar FCM Token para usuário inexistente")
    void shouldThrowResourceNotFoundWhenUpdatingFcmToken() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.updateFcmToken(userId, "fake-token")
        );
    }
}