package br.edu.ufape.alugafacil.services.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.exceptions.UserCpfDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserEmailDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;

public interface IUserService {

    UserResponse saveUser(UserRequest userRequest) throws UserCpfDuplicadoException, UserEmailDuplicadoException;

    List<UserResponse> getAllUsers();

    UserResponse getUserById(UUID id) throws UserNotFoundException;

    UserResponse updateUser(UUID id, UserRequest userRequest) throws UserNotFoundException, UserCpfDuplicadoException;

    void deleteUser(UUID id) throws UserNotFoundException;

    void updateFcmToken(UUID userId, String token) throws UserNotFoundException;

    UserResponse getUserByEmail(String email);

    UserResponse uploadProfilePicture(UUID id, MultipartFile file);
}
