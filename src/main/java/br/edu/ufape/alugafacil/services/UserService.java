package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.exceptions.UserCpfDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserEmailDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.mappers.PropertyMapper;
import br.edu.ufape.alugafacil.mappers.RealStateAgencyPropertyMapper;
import br.edu.ufape.alugafacil.mappers.UserPropertyMapper;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserPropertyMapper userPropertyMapper;
    private final PropertyMapper propertyMapper;
    private final RealStateAgencyPropertyMapper realStateAgencyPropertyMapper;

    @Override
    public UserResponse saveUser(UserRequest userRequest) throws UserCpfDuplicadoException, UserEmailDuplicadoException {
        try {

            Optional<User> byCpf = this.userRepository.findUserByCpf(userRequest.cpf());

            if (byCpf.isPresent()) {
                throw new UserCpfDuplicadoException();
            }

            User userByEmail = this.userRepository.findByEmail(userRequest.email());

            if (userByEmail.getUserId() != null && !userByEmail.getEmail().isEmpty()) {
                throw new UserEmailDuplicadoException();
            }

            User user = userPropertyMapper.toEntity(userRequest);

            List<Property> properties = userRequest.properties().stream()
                    .map(p -> {
                        Property property = propertyMapper.toEntity(p);
                        property.setUser(user);
                        return property;
                    })
                    .toList();

            user.setProperties(properties);

            RealStateAgency realStateAgency = realStateAgencyPropertyMapper.toEntity(userRequest.agency());
            user.setAgency(realStateAgency);

             User save = this.userRepository.save(user);

             return userPropertyMapper.toResponse(save);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> usuarios = this.userRepository.findAll();
        List<UserResponse> usuariosResp = usuarios.stream()
                .map(u -> userPropertyMapper.toResponse(u))
                .toList();

        return usuariosResp;
    }

    @Override
    public UserResponse getUserById(UUID id) throws UserNotFoundException {

        Optional<User> byId = this.userRepository.findById(id);

        if (!byId.isPresent()) {
            throw new UserNotFoundException();
        }

        return userPropertyMapper.toResponse(byId.get());

    }

    @Override
    public UserResponse updateUser(UUID id, UserRequest userRequest) throws UserNotFoundException , UserCpfDuplicadoException,  UserEmailDuplicadoException{

        Optional<User> byId = userRepository.findUserByCpf(userRequest.cpf());

        if (!byId.isPresent()) {
            throw new UserNotFoundException();
        }

        User user = byId.get();

        Optional<User> byCpf = this.userRepository.findUserByCpf(userRequest.cpf());

        if (byCpf.isPresent() && !byCpf.get().getUserId().equals(user.getUserId())) {
            throw new UserCpfDuplicadoException();
        }

        User userByEmail = this.userRepository.findByEmail(userRequest.email());

        if (userByEmail.getUserId() != null && !userByEmail.getEmail().isEmpty() && !userByEmail.getUserId().equals(user.getUserId())) {
            throw new UserEmailDuplicadoException();
        }

        userPropertyMapper.updateEntityFromRequest(userRequest, user);

        List<Property> properties = userRequest.properties().stream()
                .map(p -> {
                    Property property = propertyMapper.toEntity(p);
                    property.setUser(user);
                    return property;
                })
                .toList();

        user.setProperties(properties);

        RealStateAgency realStateAgency = realStateAgencyPropertyMapper.toEntity(userRequest.agency());
        user.setAgency(realStateAgency);

        User save = userRepository.save(user);

        return userPropertyMapper.toResponse(save);
    }

    @Override
    public void deleteUser(UUID id) throws UserNotFoundException {

        Optional<User> byId = userRepository.findById(id);

        if (!byId.isPresent()) {
            throw new UserNotFoundException();
        }

        this.userRepository.delete(byId.get());
    }


}
