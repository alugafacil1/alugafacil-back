package br.edu.ufape.alugafacil.models;

import br.edu.ufape.alugafacil.dto.AdressDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String street;
    private String neighborhood;
    private String city;
    private String postalCode;
    private String number;
    private String complement;


    public static Address getEntity(AdressDto adressDto) {
        Address address = Address.builder()
                .street(adressDto.getStreet())
                .neighborhood(adressDto.getNeighborhood())
                .city(adressDto.getCity())
                .postalCode(adressDto.getPostalCode())
                .number(adressDto.getNumber())
                .complement(adressDto.getComplement())
                .build();
        return address;
    }
}