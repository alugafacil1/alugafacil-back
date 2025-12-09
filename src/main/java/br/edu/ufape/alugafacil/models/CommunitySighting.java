package br.edu.ufape.alugafacil.models;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CommunitySighting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String sightingId;

    private String submitterUserId;
    private String photoUrl;
    private String phoneNumber;

    @Embedded
    private Geolocation geolocation;
}
