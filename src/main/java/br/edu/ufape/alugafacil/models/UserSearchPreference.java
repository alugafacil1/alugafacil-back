package br.edu.ufape.alugafacil.models;

import java.util.UUID;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class UserSearchPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID preferenceId;

    private String name;
    private Integer maxPriceInCents;
    private Integer minBedrooms;
    private Integer minBathrooms;
    private Boolean petFriendly;
    private Boolean furnished;
    private String city;
    private String neighborhood;
    private Integer searchRadiusInMeters;
    private Integer garageCount;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "center_lat")),
        @AttributeOverride(name = "longitude", column = @Column(name = "center_lon"))
    })
    private Geolocation searchCenter;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}