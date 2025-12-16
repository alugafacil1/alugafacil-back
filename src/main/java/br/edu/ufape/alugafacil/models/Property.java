package br.edu.ufape.alugafacil.models;

import java.util.List;

import br.edu.ufape.alugafacil.enums.PropertyStatus;
import br.edu.ufape.alugafacil.enums.PropertyType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String propertyId;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Embedded
    private Address address;

    @Embedded
    private Geolocation geolocation;

    private Integer priceInCents;
    private Integer numberOfRooms;
    private Integer numberOfBedrooms;
    private Integer numberOfBathrooms;
    private Boolean furnished;
    private Boolean petFriendly;
    private Boolean garage;
    private Boolean isOwner;
    
    private String videoUrl;
    private String phoneNumber;

    @ElementCollection
    private List<String> photoUrls;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    @Enumerated(EnumType.STRING)
    private PropertyType type;

    @ManyToOne
    @JoinColumn(name = "user_id") // Dono do imóvel
    private User user;
}
