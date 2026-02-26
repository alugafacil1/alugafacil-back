package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceRequest;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceResponse;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.exceptions.UserPreferenceNotFound;
import br.edu.ufape.alugafacil.mappers.UserMapper;
import br.edu.ufape.alugafacil.mappers.UserSearchPreferenceMapper;
import br.edu.ufape.alugafacil.models.Geolocation;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.models.UserSearchPreference;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.repositories.UserSearchPreferenceRepository;
import br.edu.ufape.alugafacil.services.interfaces.IUserPreferenceService;
import br.edu.ufape.alugafacil.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPreferenceService implements IUserPreferenceService {

    private final UserSearchPreferenceRepository userSearchPreferenceRepository;
    private final UserSearchPreferenceMapper userSearchPreferenceMapper;
    private final UserMapper userPropertyMapper;
    private final UserRepository userRepository;
    private final IUserService userService;

    @Override
    @Transactional
    public UserPreferenceResponse save(UserPreferenceRequest userPreferenceRequest) throws Exception {

        UserSearchPreference preference = this.userSearchPreferenceMapper.toEntity(userPreferenceRequest);

        User user;
        
        // CAMINHO 1: Se userId foi fornecido, busca o usuário existente
        if (userPreferenceRequest.userId() != null) {
            user = this.userRepository.findById(userPreferenceRequest.userId())
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + userPreferenceRequest.userId()));
        } 
        // CAMINHO 2: Se userId não foi fornecido, cria um novo usuário
        else {
            if (userPreferenceRequest.user() == null) {
                throw new IllegalArgumentException("É necessário fornecer 'userId' ou 'user' para criar a preferência");
            }
            UserResponse userResponse = userService.saveUser(userPreferenceRequest.user());
            user = this.userRepository.findById(userResponse.userId())
                    .orElseThrow(() -> new UserNotFoundException("Erro ao recuperar usuário criado"));
        }

        if (userPreferenceRequest.searchCenter() != null) {
            Geolocation geolocation = Geolocation.builder()
                    .latitude(userPreferenceRequest.searchCenter().latitude())
                    .longitude(userPreferenceRequest.searchCenter().longitude())
                    .build();
            preference.setSearchCenter(geolocation);
        }
        
        preference.setUser(user);

        UserSearchPreference save = this.userSearchPreferenceRepository.save(preference);

        return this.userSearchPreferenceMapper.toResponse(save);

    }

    @Override
    public List<UserPreferenceResponse> getAllPreference(UUID idUser) throws UserNotFoundException {

        Optional<User> byId = this.userRepository.findById(idUser);

        if (byId.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<UserSearchPreference> preferences = this.userSearchPreferenceRepository.findByIdUser(idUser);

        List<UserPreferenceResponse> preferenceResponses = new ArrayList<>();

        if (preferences != null && !preferences.isEmpty()) {
            preferenceResponses = preferences.stream()
                    .map(this.userSearchPreferenceMapper::toResponse)
                    .toList();
        }

        return preferenceResponses;
    }

    @Override
    public UserPreferenceResponse getPreferenceById(UUID id) throws UserPreferenceNotFound {

        Optional<UserSearchPreference> byId = this.userSearchPreferenceRepository.findById(id);

        if (byId.isEmpty()) {
            throw new UserPreferenceNotFound();
        }

        return this.userSearchPreferenceMapper.toResponse(byId.get());
    }

    @Override
    public UserPreferenceResponse updatePreference(UUID id, UserPreferenceRequest userPreferenceRequest)
            throws Exception {

        Optional<UserSearchPreference> byId = this.userSearchPreferenceRepository.findById(id);

        if (byId.isEmpty()) {
            throw new UserPreferenceNotFound();
        }

        UserSearchPreference userSearchPreference = byId.get();

        this.userSearchPreferenceMapper.updateEntityFromRequest(userPreferenceRequest, userSearchPreference);

        User user;
        
        // CAMINHO 1: Se userId foi fornecido, busca o usuário existente
        if (userPreferenceRequest.userId() != null) {
            user = this.userRepository.findById(userPreferenceRequest.userId())
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + userPreferenceRequest.userId()));
        } 
        // CAMINHO 2: Se userId não foi fornecido, cria um novo usuário
        else {
            if (userPreferenceRequest.user() == null) {
                throw new IllegalArgumentException("É necessário fornecer 'userId' ou 'user' para atualizar a preferência");
            }
            UserResponse userResponse = userService.saveUser(userPreferenceRequest.user());
            user = this.userRepository.findById(userResponse.userId())
                    .orElseThrow(() -> new UserNotFoundException("Erro ao recuperar usuário criado"));
        }

        if (userPreferenceRequest.searchCenter() != null) {
            Geolocation geolocation = Geolocation.builder()
                    .latitude(userPreferenceRequest.searchCenter().latitude())
                    .longitude(userPreferenceRequest.searchCenter().longitude())
                    .build();
            userSearchPreference.setSearchCenter(geolocation);
        }
        
        userSearchPreference.setUser(user);

        UserSearchPreference update = this.userSearchPreferenceRepository.save(userSearchPreference);

        return this.userSearchPreferenceMapper.toResponse(update);
    }

    @Override
    public void deleteUser(UUID id) throws UserPreferenceNotFound {

        Optional<UserSearchPreference> byId = this.userSearchPreferenceRepository.findById(id);

        if (byId.isEmpty()) {
            throw new UserPreferenceNotFound();
        }

        this.userSearchPreferenceRepository.delete(byId.get());
    }
}
