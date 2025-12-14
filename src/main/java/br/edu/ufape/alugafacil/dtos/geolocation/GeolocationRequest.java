package br.edu.ufape.alugafacil.dtos.geolocation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GeolocationRequest(
    @NotNull(message = "Latitude é obrigatória")
    @Min(value = -90, message = "Latitude inválida")
    @Max(value = 90, message = "Latitude inválida")
    Double latitude,

    @NotNull(message = "Longitude é obrigatória")
    @Min(value = -180, message = "Longitude inválida")
    @Max(value = 180, message = "Longitude inválida")
    Double longitude
) {}
