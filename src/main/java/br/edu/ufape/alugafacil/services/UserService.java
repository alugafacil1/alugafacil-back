package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.Subscription;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.dto.UserDto;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void saveUser(UserDto userDto) throws UserNotFoundException {
        throw new UnsupportedOperationException("Método ainda não implementado");
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public UserDto getUserById(String id) throws UserNotFoundException {
        throw new UnsupportedOperationException("Método ainda não implementado");
    }

    @Override
    public void updateUser(String id, UserDto userDto) throws UserNotFoundException {
        throw new UnsupportedOperationException("Método ainda não implementado");
    }

    @Override
    public void deleteUser(String id) throws UserNotFoundException {
        throw new UnsupportedOperationException("Método ainda não implementado");
    }

    public User getEntity(UserDto userDto) {

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .cpf(userDto.getCpf())
                .creciNumber(userDto.getCreciNumber())
                .phoneNumber(userDto.getPhoneNumber())
                .userType(userDto.getUserType())
                .build();

        RealStateAgency realStateAgency = RealStateAgency.getEntity(userDto.getAgency());
        user.setAgency(realStateAgency);

        List<Subscription> subscriptions = userDto.getSubscriptionsDtos().stream()
                .map(Subscription::getEntity)
                .toList();
        user.setSubscriptions(subscriptions);


        return user;
    }
}
