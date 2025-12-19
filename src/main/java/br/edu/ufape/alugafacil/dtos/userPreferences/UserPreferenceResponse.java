package br.edu.ufape.alugafacil.dtos.userPreferences;

import br.edu.ufape.alugafacil.dtos.geolocalionResponse.GeolocationResponse;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;

import java.util.UUID;

public record UserPreferenceResponse(
        UUID preferenceId,
        String name,
        Integer maxPriceInCents,
        Integer minBedrooms,
        Integer minBathrooms,
        Boolean petFriendly,
        Boolean furnished,
        String city,
        String neighborhood,
        Integer searchRadiusInMeters,
        Integer garageCount,
        GeolocationResponse searchCenter,
        UserResponse user
) { }
