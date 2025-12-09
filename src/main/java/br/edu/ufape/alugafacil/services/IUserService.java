package br.edu.ufape.alugafacil.services;

import java.util.List;
import br.edu.ufape.alugafacil.dto.UserDto;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.models.User;

public interface IUserService {

    void saveUser(UserDto userDto) throws UserNotFoundException;

    List<User> getAllUsers();

    UserDto getUserById(String id) throws UserNotFoundException;

    void updateUser(String id, UserDto userDto) throws UserNotFoundException;

    void deleteUser(String id) throws UserNotFoundException;
}
