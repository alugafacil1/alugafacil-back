package br.edu.ufape.alugafacil.dto;


import br.edu.ufape.alugafacil.enums.PropertyStatus;
import br.edu.ufape.alugafacil.enums.PropertyType;

import lombok.Data;

import java.util.List;

@Data
public class PropertyDto {

    private String propertyId;
    private String title;
    private String description;
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
    private List<String> photoUrls;
    private PropertyStatus status;
    private PropertyType type;
    private GeolocationDto geolocation;
    private AdressDto address;
    
}