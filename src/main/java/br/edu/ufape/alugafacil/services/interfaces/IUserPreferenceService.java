package br.edu.ufape.alugafacil.services.interfaces;

import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceRequest;
import br.edu.ufape.alugafacil.dtos.userPreferences.UserPreferenceResponse;
import br.edu.ufape.alugafacil.exceptions.UserCpfDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserEmailDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.exceptions.UserPreferenceNotFound;

import java.util.List;
import java.util.UUID;

public interface IUserPreferenceService {

    UserPreferenceResponse save(UserPreferenceRequest userPreferenceRequest) throws Exception;

    List<UserPreferenceResponse> getAllPreference(UUID idUser) throws UserNotFoundException;

    UserPreferenceResponse getPreferenceById(UUID id) throws UserPreferenceNotFound;

    UserPreferenceResponse updatePreference(UUID id, UserPreferenceRequest userPreferenceRequest) throws Exception;

    void deleteUser(UUID id) throws UserPreferenceNotFound;
}
