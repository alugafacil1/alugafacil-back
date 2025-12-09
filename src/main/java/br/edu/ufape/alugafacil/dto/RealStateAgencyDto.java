package br.edu.ufape.alugafacil.dto;


import lombok.Data;

@Data
public class RealStateAgencyDto {

    private String agencyId;
    private String name;
    private String corporateName;
    private String email;
    private String photoUrl;
    private String cnpj;
    private String website;
    private String phoneNumber;
}
