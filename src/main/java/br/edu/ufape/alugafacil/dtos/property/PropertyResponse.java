package br.edu.ufape.alugafacil.dtos.property;

import java.util.List;
import java.util.UUID;

import br.edu.ufape.alugafacil.dtos.address.AddressResponse;
import br.edu.ufape.alugafacil.dtos.geolocalionResponse.GeolocationResponse;
import br.edu.ufape.alugafacil.models.enums.PropertyStatus;
import br.edu.ufape.alugafacil.models.enums.PropertyType;

public record PropertyResponse(
    UUID propertyId,
    String title,
    String description,
    AddressResponse address,
    GeolocationResponse geolocation,
    Integer priceInCents,
    Integer numberOfRooms,
    Integer numberOfBedrooms,
    Integer numberOfBathrooms,
    Boolean furnished,
    Boolean petFriendly,
    Boolean garage,
    Boolean isOwner,
    String videoUrl,
    String phoneNumber,
    List<String> photoUrls,
    PropertyStatus status,
    PropertyType type,
    UUID ownerId
) {}
