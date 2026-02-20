package br.edu.ufape.alugafacil.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyResponse;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.mappers.RealStateAgencyPropertyMapper;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.repositories.RealStateAgencyRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealStateAgencyService {
    
    private final RealStateAgencyRepository realStateAgencyRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final RealStateAgencyPropertyMapper mapper;

    @Transactional(readOnly = true)
    public Page<RealStateAgencyResponse> getAllRealStateAgencies(Pageable pageable) {
        return realStateAgencyRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public RealStateAgencyResponse getRealStateAgencyById(UUID agencyId) {
        RealStateAgency agency = getAgencyOrThrow(agencyId);
        return mapper.toResponse(agency);
    }

    @Transactional
    public RealStateAgencyResponse createRealStateAgency(RealStateAgencyRequest request) {
        RealStateAgency newAgency = mapper.toEntity(request);
        
        RealStateAgency savedAgency = realStateAgencyRepository.save(newAgency);
        
        return mapper.toResponse(savedAgency);
    }

    @Transactional
    public RealStateAgencyResponse updateRealStateAgency(UUID agencyId, RealStateAgencyRequest request) {
        RealStateAgency existingAgency = getAgencyOrThrow(agencyId);
        
        mapper.updateEntityFromRequest(request, existingAgency);
        
        RealStateAgency updatedAgency = realStateAgencyRepository.save(existingAgency);
        return mapper.toResponse(updatedAgency);
    }

    @Transactional
    public void deleteRealStateAgency(UUID agencyId) {
        if (!realStateAgencyRepository.existsById(agencyId)) {
            throw new IllegalArgumentException("Agência não encontrada.");
        }
        realStateAgencyRepository.deleteById(agencyId);
    }

    @Transactional
    public void addMember(UUID agencyId, UUID userId, UUID actingUserId) {
        RealStateAgency agency = getAgencyOrThrow(agencyId);
        User user = getUserOrThrow(userId);
        
        validateAgencyAdminOrSystemAdmin(agency, actingUserId);

        if (user.getUserType() != UserType.REALTOR) {
            throw new IllegalArgumentException("Apenas usuários do tipo CORRETOR (REALTOR) podem ser adicionados como membros.");
        }

        user.setAgency(agency);
        userRepository.save(user);
    }

    @Transactional
    public void removeMember(UUID agencyId, UUID userId, UUID actingUserId) {
        RealStateAgency agency = getAgencyOrThrow(agencyId);
        User user = getUserOrThrow(userId);

        validateAgencyAdminOrSystemAdmin(agency, actingUserId);

        if (user.getAgency() == null || !agency.getAgencyId().equals(user.getAgency().getAgencyId())) {
            throw new IllegalArgumentException("Usuário não é um membro desta agência.");
        }

        if (agency.getUser() != null && agency.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Não é possível remover o proprietário da agência.");
        }

        user.setAgency(null);
        userRepository.save(user);
    }

    @Transactional
    public void transferProperty(UUID agencyId, UUID propertyId, UUID targetUserId, UUID actingUserId) {
        RealStateAgency agency = getAgencyOrThrow(agencyId);
        User targetUser = getUserOrThrow(targetUserId);
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado."));

        validateAgencyAdminOrSystemAdmin(agency, actingUserId);

        if (targetUser.getAgency() == null || !agency.getAgencyId().equals(targetUser.getAgency().getAgencyId())) {
            throw new IllegalArgumentException("O usuário destino deve ser membro desta agência.");
        }

        if (property.getUser() == null || property.getUser().getAgency() == null || !agency.getAgencyId().equals(property.getUser().getAgency().getAgencyId())) {
            throw new IllegalArgumentException("O responsável atual pelo imóvel deve pertencer à mesma agência.");
        }

        property.setUser(targetUser);
        propertyRepository.save(property);
    }

    // =========================================================================
    // MÉTODOS PRIVADOS AUXILIARES
    // =========================================================================

    private RealStateAgency getAgencyOrThrow(UUID agencyId) {
        return realStateAgencyRepository.findById(agencyId)
                .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada com o ID fornecido."));
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    private void validateAgencyAdminOrSystemAdmin(RealStateAgency agency, UUID actingUserId) {
        User actingUser = getUserOrThrow(actingUserId);
        
        boolean isOwner = agency.getUser() != null && agency.getUser().getUserId().equals(actingUserId);
        boolean isAdmin = actingUser.getUserType() == UserType.ADMIN;
        
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("Usuário não autorizado para realizar esta ação na agência.");
        }
    }
}