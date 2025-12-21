package br.edu.ufape.alugafacil.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.repositories.RealStateAgencyRepository;
import br.edu.ufape.alugafacil.repositories.UserRepository;

@Service
public class RealStateAgencyService  {
    
    @Autowired
    private RealStateAgencyRepository realStateAgencyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

     public List<RealStateAgency> getAllRealStateAgencies() {
        return realStateAgencyRepository.findAll();
    }

    public RealStateAgency getRealStateAgencyById(UUID agencyId) {
        return realStateAgencyRepository.findById(agencyId).orElse(null);
    }

    public List<User> getMembers(UUID agencyId) {
        return userRepository.findByAgencyId(agencyId);
    }

    public RealStateAgency createRealStateAgency(RealStateAgencyRequest realStateAgency) {
        RealStateAgency newRealStateAgency = new RealStateAgency();

        newRealStateAgency.setName(realStateAgency.getName());
        newRealStateAgency.setCorporateName(realStateAgency.getCorporateName());
        newRealStateAgency.setEmail(realStateAgency.getEmail());
        newRealStateAgency.setPhotoUrl(realStateAgency.getPhotoUrl());
        newRealStateAgency.setCnpj(realStateAgency.getCnpj());
        newRealStateAgency.setWebsite(realStateAgency.getWebsite());
        newRealStateAgency.setPhoneNumber(realStateAgency.getPhoneNumber());

        return realStateAgencyRepository.save(newRealStateAgency);
    }

    public RealStateAgency updateRealStateAgency(UUID agencyId, RealStateAgencyRequest realStateAgency) {
        RealStateAgency existingAgency = realStateAgencyRepository.findById(agencyId).orElse(null);
        
        if (existingAgency != null) {
            existingAgency.setName(realStateAgency.getName());
            existingAgency.setCorporateName(realStateAgency.getCorporateName());
            existingAgency.setEmail(realStateAgency.getEmail());
            existingAgency.setPhotoUrl(realStateAgency.getPhotoUrl());
            existingAgency.setCnpj(realStateAgency.getCnpj());
            existingAgency.setWebsite(realStateAgency.getWebsite());
            existingAgency.setPhoneNumber(realStateAgency.getPhoneNumber());

            return realStateAgencyRepository.save(existingAgency);
        }
        return null;
    }

    public void deleteRealStateAgency(UUID agencyId) {
        realStateAgencyRepository.deleteById(agencyId);
    }

    public void addMember(UUID agencyId, UUID userId, UUID actingUserId) {
        RealStateAgency agency = realStateAgencyRepository.findById(agencyId).orElseThrow(() -> new IllegalArgumentException("Agency not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User actingUser = userRepository.findById(actingUserId).orElseThrow(() -> new IllegalArgumentException("Acting user not found"));

        boolean isOwner = agency.getUser() != null && agency.getUser().getUserId().equals(actingUserId);
        boolean isAdmin = actingUser.getUserType() == UserType.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("Not authorized to add members");
        }

        if (user.getUserType() != UserType.REALTOR) {
            throw new IllegalArgumentException("Only users with REALTOR type can be added as members");
        }

        user.setAgency(agency);
        userRepository.save(user);
    }

    public void removeMember(UUID agencyId, UUID userId, UUID actingUserId) {
        RealStateAgency agency = realStateAgencyRepository.findById(agencyId).orElseThrow(() -> new IllegalArgumentException("Agency not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User actingUser = userRepository.findById(actingUserId).orElseThrow(() -> new IllegalArgumentException("Acting user not found"));

        boolean isOwner = agency.getUser() != null && agency.getUser().getUserId().equals(actingUserId);
        boolean isAdmin = actingUser.getUserType() == UserType.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("Not authorized to remove members");
        }

        if (user.getAgency() == null || !agency.getAgencyId().equals(user.getAgency().getAgencyId())) {
            throw new IllegalArgumentException("User is not a member of the agency");
        }

        if (agency.getUser() != null && agency.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Cannot remove agency owner");
        }

        user.setAgency(null);
        userRepository.save(user);
    }

    public void transferProperty(UUID agencyId, UUID propertyId, UUID targetUserId, UUID actingUserId) {
        RealStateAgency agency = realStateAgencyRepository.findById(agencyId).orElseThrow(() -> new IllegalArgumentException("Agency not found"));
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new IllegalArgumentException("Property not found"));
        User target = userRepository.findById(targetUserId).orElseThrow(() -> new IllegalArgumentException("Target user not found"));
        User actingUser = userRepository.findById(actingUserId).orElseThrow(() -> new IllegalArgumentException("Acting user not found"));

        boolean isOwner = agency.getUser() != null && agency.getUser().getUserId().equals(actingUserId);
        boolean isAdmin = actingUser.getUserType() == UserType.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("Not authorized to transfer property");
        }

        if (target.getAgency() == null || !agency.getAgencyId().equals(target.getAgency().getAgencyId())) {
            throw new IllegalArgumentException("Target user must be a member of the agency");
        }

        if (property.getUser() == null || property.getUser().getAgency() == null || !agency.getAgencyId().equals(property.getUser().getAgency().getAgencyId())) {
            throw new IllegalArgumentException("Property responsible must belong to the same agency");
        }

        property.setUser(target);
        propertyRepository.save(property);
    }
}
