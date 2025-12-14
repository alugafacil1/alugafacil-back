package br.edu.ufape.alugafacil.dtos.address;

public record AddressResponse(
    String street,
    String city,
    String state,
    String zipCode,
    String number,
    String neighborhood
) {}
