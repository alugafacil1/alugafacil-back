package br.edu.ufape.alugafacil.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.enums.UserStatus;
import br.edu.ufape.alugafacil.exceptions.ResourceNotFoundException;
import br.edu.ufape.alugafacil.exceptions.UserCpfDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserEmailDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.mappers.RealStateAgencyMapper;
import br.edu.ufape.alugafacil.mappers.UserMapper;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import br.edu.ufape.alugafacil.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userPropertyMapper;
    private final RealStateAgencyMapper agencyMapper;
    private final SubscriptionService subscriptionService;
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
    private final PropertyRepository propertyRepository;

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

        // subscriptionService.createInitialFreeSubscription(savedUser);

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
    public Page<UserResponse> getAllUsers(Pageable pageable) {
    	return userRepository.findAll(pageable)
                .map(userPropertyMapper::toResponse);
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

    @Override
    @Transactional
    public void updateFcmToken(UUID userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + userId));
        
        user.setFcmToken(token);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userPropertyMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o email: " + email));
    }

    @Override
    @Transactional
    public UserResponse uploadProfilePicture(UUID id, MultipartFile file) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        try {
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = id.toString() + "." + extension;
            Path targetLocation = fileStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/users/uploads/")
                    .path(fileName)
                    .toUriString();

            user.setPhotoUrl(fileUrl);
            return userPropertyMapper.toResponse(userRepository.save(user));

        } catch (IOException ex) {
            throw new RuntimeException("Erro ao salvar arquivo " + file.getOriginalFilename(), ex);
        }
    }
    
    @Override
    @Transactional
    public void updateUserStatus(UUID id, UserStatus newStatus) throws UserNotFoundException {
    	User u = userRepository.findById(id)
    			.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    	
    	u.setStatus(newStatus);
    	
    	if (newStatus == UserStatus.BLOCKED) {
    		propertyRepository.pauseActivePropertiesByUserId(id);
    	}
    	
    	userRepository.save(u);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "jpg";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

