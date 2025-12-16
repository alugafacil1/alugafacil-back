package br.edu.ufape.alugafacil.models;


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

}