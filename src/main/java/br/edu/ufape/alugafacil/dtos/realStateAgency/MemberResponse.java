package br.edu.ufape.alugafacil.dtos.realStateAgency;

import java.util.UUID;

import br.edu.ufape.alugafacil.enums.UserType;
import lombok.Data;

@Data
public class MemberResponse {
    private UUID userId;
    private String name;
    private String email;
    private UserType userType;
}
