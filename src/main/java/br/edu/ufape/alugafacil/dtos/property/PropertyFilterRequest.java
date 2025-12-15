package br.edu.ufape.alugafacil.dtos.property;

import br.edu.ufape.alugafacil.models.enums.PropertyStatus;
import br.edu.ufape.alugafacil.models.enums.PropertyType;
import lombok.Data;

@Data
public class PropertyFilterRequest {
	private Integer minPrice;
	private Integer maxPrice;
	
	private Integer minRooms;
	private Integer minBedrooms;
	
	private Boolean garage;
	private Boolean furnished;
	private Boolean petFriendly;
	
	private PropertyType type;
    private PropertyStatus status;
    
    
    
    // Localização
    private String city;
    private String neighborhood;
    
    private Double lat;
    private Double lon;
    private Double radius;
}
