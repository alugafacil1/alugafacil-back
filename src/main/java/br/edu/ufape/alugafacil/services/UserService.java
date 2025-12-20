package br.edu.ufape.alugafacil.services;

import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.exceptions.UserCpfDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserEmailDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.mappers.RealStateAgencyPropertyMapper;
import br.edu.ufape.alugafacil.mappers.UserPropertyMapper;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserPropertyMapper userPropertyMapper;
    private final RealStateAgencyPropertyMapper agencyMapper;
    private final SubscriptionService subscriptionService;

    @Override
    @Transactional
    public UserResponse saveUser(UserRequest request) {
        validateCpfDuplicate(request.cpf(), null);
        validateEmailDuplicate(request.email(), null);

        User user = userPropertyMapper.toEntity(request);

        if (request.agency() != null) {
            user.setAgency(agencyMapper.toEntity(request.agency()));
        }

        User savedUser = userRepository.save(user);

        subscriptionService.createInitialFreeSubscription(savedUser);

        return userPropertyMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        validateCpfDuplicate(request.cpf(), id);
        validateEmailDuplicate(request.email(), id);

        userPropertyMapper.updateEntityFromRequest(request, user);

        if (request.agency() != null) {
            if (user.getAgency() == null) {
                RealStateAgency newAgency = agencyMapper.toEntity(request.agency());
                user.setAgency(newAgency);
            } else {
                agencyMapper.updateEntityFromRequest(request.agency(), user.getAgency());
            }
        }
        
        return userPropertyMapper.toResponse(userRepository.save(user));
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userPropertyMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userPropertyMapper::toResponse)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    private void validateCpfDuplicate(String cpf, UUID userIdToIgnore) {
        userRepository.findUserByCpf(cpf).ifPresent(existingUser -> {
            if (userIdToIgnore == null || !existingUser.getUserId().equals(userIdToIgnore)) {
                throw new UserCpfDuplicadoException();
            }
        });
    }

    private void validateEmailDuplicate(String email, UUID userIdToIgnore) {
        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if (userIdToIgnore == null || !existingUser.getUserId().equals(userIdToIgnore)) {
                throw new UserEmailDuplicadoException();
            }
        });
    }
}