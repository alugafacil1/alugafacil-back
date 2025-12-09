package br.edu.ufape.alugafacil.dto;

import br.edu.ufape.alugafacil.enums.UserType;
import br.edu.ufape.alugafacil.dto.RealStateAgencyDto;
import br.edu.ufape.alugafacil.dto.SubscriptionDto;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String name;
    private String email;
    private String photoUrl;
    private String cpf;
    private String creciNumber;
    private String password;
    private String phoneNumber;
    private UserType userType;
    private RealStateAgencyDto agency;
    private List<SubscriptionDto> subscriptionsDtos;
    private List<PropertyDto> propertiesDtos;
}
