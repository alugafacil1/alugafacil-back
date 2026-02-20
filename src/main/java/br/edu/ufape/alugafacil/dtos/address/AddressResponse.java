package br.edu.ufape.alugafacil.dtos.address;

public record AddressResponse(
    String street,
    String city,
    String state,
    String postalCode,
    String number,
    String neighborhood,
    String complement 
) {}