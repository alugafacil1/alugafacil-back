package br.edu.ufape.alugafacil.models;

import java.util.UUID;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("LISTING")
@Data
@EqualsAndHashCode(callSuper = true)
public class ListingNotification extends Notification {
    private UUID propertyId;
    private String alertName;
}