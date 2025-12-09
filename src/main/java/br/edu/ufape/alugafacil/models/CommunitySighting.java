package br.edu.ufape.alugafacil.models;

import java.util.UUID;

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
    private UUID sightingId;

    private UUID submitterUserId;
    private String photoUrl;
    private String phoneNumber;

    @Embedded
    private Geolocation geolocation;
}
