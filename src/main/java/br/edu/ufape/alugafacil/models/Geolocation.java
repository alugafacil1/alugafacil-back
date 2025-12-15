package br.edu.ufape.alugafacil.models;

import br.edu.ufape.alugafacil.dto.GeolocationDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Geolocation {
    private Double latitude;
    private Double longitude;

    public static Geolocation getEntity(GeolocationDto geolocationDto) {
        Geolocation geolocation = Geolocation.builder()
                .latitude(geolocationDto.getLatitude())
                .longitude(geolocationDto.getLongitude())
                .build();
        return geolocation;
    }
}
