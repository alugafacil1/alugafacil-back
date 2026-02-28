package br.edu.ufape.alugafacil.dtos.userPreferences;

import br.edu.ufape.alugafacil.dtos.geolocation.GeolocationRequest;
import br.edu.ufape.alugafacil.dtos.user.UserRequest;

import java.util.UUID;


public record UserPreferenceRequest(
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
         GeolocationRequest searchCenter,
         UUID userId,
         UserRequest user
) { }
