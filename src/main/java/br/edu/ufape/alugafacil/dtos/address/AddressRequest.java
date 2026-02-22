package br.edu.ufape.alugafacil.dtos.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequest(
    @NotBlank(message = "A rua é obrigatória")
    String street,

    @NotBlank(message = "O bairro é obrigatório")
    String neighborhood,

    @NotBlank(message = "A cidade é obrigatória")
    String city,

    @NotBlank(message = "O estado é obrigatório")
    String state,

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido") 
    String postalCode,

    @NotBlank(message = "O número é obrigatório") 
    String number,

    String complement
) {}
