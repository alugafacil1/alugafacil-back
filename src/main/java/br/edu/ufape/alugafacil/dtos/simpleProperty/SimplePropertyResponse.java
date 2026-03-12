package br.edu.ufape.alugafacil.dtos.simpleProperty;

import java.util.List;
import java.util.UUID;

import br.edu.ufape.alugafacil.dtos.address.AddressResponse;
import br.edu.ufape.alugafacil.dtos.geolocalionResponse.GeolocationResponse;
import br.edu.ufape.alugafacil.enums.PropertyType;

public record SimplePropertyResponse(
    UUID id,
    PropertyType propertyType,
    String phoneNumber,
    AddressResponse address,
    GeolocationResponse geolocation,
    List<String> photoUrls
) {}
