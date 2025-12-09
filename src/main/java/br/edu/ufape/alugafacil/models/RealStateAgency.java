package br.edu.ufape.alugafacil.models;

import br.edu.ufape.alugafacil.dto.RealStateAgencyDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealStateAgency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String agencyId;

    private String name;
    private String corporateName;
    private String email;
    private String photoUrl;
    private String cnpj;
    private String website;
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static RealStateAgency getEntity(RealStateAgencyDto realStateAgencyDto) {
        RealStateAgency realStateAgency = RealStateAgency.builder()
                .name(realStateAgencyDto.getName())
                .email(realStateAgencyDto.getEmail())
                .cnpj(realStateAgencyDto.getCnpj())
                .phoneNumber(realStateAgencyDto.getPhoneNumber())
                .corporateName(realStateAgencyDto.getCorporateName())
                .photoUrl(realStateAgencyDto.getPhotoUrl())
                .website(realStateAgencyDto.getWebsite())
                .agencyId(realStateAgencyDto.getAgencyId()!=null && !realStateAgencyDto.getAgencyId().isEmpty() ? realStateAgencyDto.getAgencyId() : null)
                .build();
        return realStateAgency;
    }
}
