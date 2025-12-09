package br.edu.ufape.alugafacil.dtos.RealStateAgency;

import lombok.Data;

@Data
public class RealStateAgencyRequest {
    private String name;
    private String corporateName;
    private String email;
    private String photoUrl;
    private String cnpj;
    private String website;
    private String phoneNumber;
}
