package br.edu.ufape.alugafacil.dtos.user;

import java.util.List;

import br.edu.ufape.alugafacil.dtos.property.PropertyRequest;
import br.edu.ufape.alugafacil.dtos.realStateAgency.RealStateAgencyRequest;
import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.models.Subscription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest (
        @NotBlank(message = "O nome é obrigatório")
        String name,
        @NotBlank(message = "O email é obrigatório")
        String email,
        String photoUrl,
        @NotBlank(message = "O cpf é obrigatório")
        String cpf,
        String creciNumber,
        @NotBlank(message = "O senha é obrigatória")
        String passwordHash,
        @NotBlank(message = "O telefone é obrigatória")
        String phoneNumber,
        @NotNull(message = "É obrigatório selecionar o tipo de usuário ")
        UserType userType,
        RealStateAgencyRequest agency,
        List<PropertyRequest> properties,
        List<Subscription> subscriptions
) { }
