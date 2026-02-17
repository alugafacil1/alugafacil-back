package br.edu.ufape.alugafacil.dtos.realStateAgency;

import java.util.List;
import java.util.UUID;

import br.edu.ufape.alugafacil.enums.AgencyStatus;
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
    private AgencyStatus status;

    private List<MemberResponse> members;
}
