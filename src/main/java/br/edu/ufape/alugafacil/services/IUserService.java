package br.edu.ufape.alugafacil.services;

import java.util.List;
import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.exceptions.UserCpfDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserEmailDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.models.User;

public interface IUserService {

    UserResponse saveUser(UserRequest userRequest) throws UserCpfDuplicadoException, UserEmailDuplicadoException;

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String id) throws UserNotFoundException;

    UserResponse updateUser(String id, UserRequest userRequest) throws UserNotFoundException, UserCpfDuplicadoException;

    void deleteUser(String id) throws UserNotFoundException;
}
