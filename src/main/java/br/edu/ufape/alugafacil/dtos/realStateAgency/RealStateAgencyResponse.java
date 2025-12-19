package br.edu.ufape.alugafacil.dtos.realStateAgency;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class RealStateAgencyResponse {
    private UUID agencyId;
    private String name;
    private String corporateName;
    private String email;
    private String photoUrl;
    private String cnpj;
    private String website;
    private String phoneNumber;

    private List<MemberResponse> members;
}
