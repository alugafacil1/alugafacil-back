package br.edu.ufape.alugafacil.dtos.simpleProperty;

import java.util.List;

import br.edu.ufape.alugafacil.dtos.address.AddressRequest;
import br.edu.ufape.alugafacil.dtos.geolocation.GeolocationRequest;
import br.edu.ufape.alugafacil.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SimplePropertyRequest(
    @NotNull(message = "O tipo do imóvel é obrigatório")
    PropertyType propertyType,

    @NotBlank(message = "Telefone para contato é obrigatório")
    String phoneNumber,

    @NotNull(message = "Endereço é obrigatório")
    AddressRequest address,

    @NotNull(message = "Geolocalização é obrigatória")
    GeolocationRequest geolocation,

    List<String> photoUrls
) {}
