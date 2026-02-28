package br.edu.ufape.alugafacil.services;


import br.edu.ufape.alugafacil.dtos.geolocation.GeolocationRequest;
import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceRequest;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceResponse;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.exceptions.UserPreferenceNotFound;
import br.edu.ufape.alugafacil.mappers.UserMapper;
import br.edu.ufape.alugafacil.mappers.UserSearchPreferenceMapper;
import br.edu.ufape.alugafacil.models.Geolocation;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.models.UserSearchPreference;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.repositories.UserSearchPreferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPreferenceServiceTest {

    @InjectMocks
    private UserPreferenceService service;

    @Mock
    private UserSearchPreferenceRepository userSearchPreferenceRepository;

    @Mock
    private UserSearchPreferenceMapper userSearchPreferenceMapper;

    @Mock
    private UserMapper userPropertyMapper;

    @Mock
    private UserRepository userRepository;

    private UserPreferenceRequest request;
    private UserPreferenceResponse response;
    private UserSearchPreference entity;

    @BeforeEach
    void setUp() {

        GeolocationRequest geolocationRequest =
                new GeolocationRequest(-8.05, -34.90);

        UserRequest userRequest = new UserRequest(
                "Usuário Teste",
                "teste@email.com",
                "123456",
                "00000000000",
                "81999999999",
                UserType.TENANT,
                null,
                null,
                null
        );

        request = new UserPreferenceRequest(
                "Busca Recife",
                300000,
                2,
                1,
                true,
                false,
                "Recife",
                "Boa Viagem",
                5000,
                1,
                geolocationRequest,
                null,
                userRequest
        );

        entity = new UserSearchPreference();
        response = new UserPreferenceResponse(
                UUID.randomUUID(),
                "Busca Recife",
                300000,
                2,
                1,
                true,
                false,
                "Recife",
                "Boa Viagem",
                5000,
                1,
                null,
                null
        );
    }

    @Test
    void shouldSaveUserPreference() throws Exception {

        when(userSearchPreferenceMapper.toEntity(any(UserPreferenceRequest.class)))
                .thenReturn(entity);

        when(userPropertyMapper.toEntity(any(UserRequest.class)))
                .thenReturn(new User());

        when(userSearchPreferenceRepository.save(any(UserSearchPreference.class)))
                .thenReturn(entity);

        when(userSearchPreferenceMapper.toResponse(entity))
                .thenReturn(response);

        UserPreferenceResponse result = service.save(request);

        assertNotNull(result);
        assertEquals("Busca Recife", result.name());

        verify(userSearchPreferenceRepository).save(any());
    }

    @Test
    @DisplayName("Deve retornar todas as preferências de um usuário existente")
    void shouldGetAllPreferencesByUser() throws UserNotFoundException {

        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User()));

        when(userSearchPreferenceRepository.findByIdUser(userId))
                .thenReturn(List.of(entity));

        when(userSearchPreferenceMapper.toResponse(entity))
                .thenReturn(response);

        List<UserPreferenceResponse> result =
                service.getAllPreference(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Busca Recife", result.get(0).name());

        verify(userRepository).findById(userId);
        verify(userSearchPreferenceRepository).findByIdUser(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar preferência inexistente")
    void shouldThrowExceptionWhenPreferenceNotFound() {

        UUID preferenceId = UUID.randomUUID();

        when(userSearchPreferenceRepository.findById(preferenceId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserPreferenceNotFound.class,
                () -> service.getPreferenceById(preferenceId)
        );

        verify(userSearchPreferenceRepository).findById(preferenceId);
    }

}
