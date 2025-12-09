package br.edu.ufape.alugafacil.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Geolocation {
    private Double latitude;
    private Double longitude;
}
