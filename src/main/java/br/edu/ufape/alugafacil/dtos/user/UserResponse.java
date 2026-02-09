package br.edu.ufape.alugafacil.dtos.user;

import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.RealStateAgency;
import br.edu.ufape.alugafacil.models.Subscription;

import java.util.List;
import java.util.UUID;

public record UserResponse  (
        UUID userId,
        String name,
        String email,
        String photoUrl,
        String cpf,
        String creciNumber,
        String password,
        String phoneNumber,
        UserType userType,
        RealStateAgency agency
        // List<Property> properties,
        // List<Subscription> subscriptions
){}
