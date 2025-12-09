package br.edu.ufape.alugafacil.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String street;
    private String neighborhood;
    private String city;
    private String postalCode;
    private String number;
    private String complement;
}