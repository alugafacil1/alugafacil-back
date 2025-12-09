package br.edu.ufape.alugafacil.dto;

import lombok.Data;

@Data
public class AdressDto { 

    private String street;
    private String neighborhood;
    private String city;
    private String postalCode;
    private String number;
    private String complement;

}