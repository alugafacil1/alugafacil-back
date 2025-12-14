package br.edu.ufape.alugafacil.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.ufape.alugafacil.dtos.address.AddressResponse;
import br.edu.ufape.alugafacil.dtos.geolocalionResponse.GeolocationResponse;
import br.edu.ufape.alugafacil.dtos.property.PropertyRequest;
import br.edu.ufape.alugafacil.dtos.property.PropertyResponse;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.services.interfaces.IPropertyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService implements IPropertyService {
	
	private final PropertyRepository propertyRepository;
	// private final UserRepository userRepository;
	
	@Override
	@Transactional
	public PropertyResponse createProperty(PropertyRequest request) {
		
		// User owner = userRepository.findById(request.userId())
		// 		.orElseThrow(() -> new RuntimeException("Usuàrio nâo encontrado com ID: " + request.userId()));
		
		
		return null;
	}

	@Override
	public PropertyResponse getPropertyById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyResponse> getAllProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyResponse> getPropertiesByUserId(UUID userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyResponse updateProperty(UUID id, PropertyRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteProperty(UUID id) {
		// TODO Auto-generated method stub
		
	}
	
	public PropertyResponse convertToResponse(Property property) {
	    AddressResponse addressResponse = null;
	    if (property.getAddress() != null) {
	        addressResponse = new AddressResponse(
	            property.getAddress().getStreet(),
	            property.getAddress().getNeighborhood(),
	            property.getAddress().getCity(),
	            property.getAddress().getPostalCode(),
	            property.getAddress().getNumber(),
	            property.getAddress().getComplement()
	        );
	    }

	    GeolocationResponse geolocationResponse = null;
	    if (property.getGeolocation() != null) {
	        geolocationResponse = new GeolocationResponse(
	            property.getGeolocation().getLatitude(),
	            property.getGeolocation().getLongitude()
	        );
	    }

	    return new PropertyResponse(
	        property.getPropertyId(),
	        property.getTitle(),
	        property.getDescription(),
	        addressResponse,
	        geolocationResponse,
	        property.getPriceInCents(),
	        property.getNumberOfRooms(),
	        property.getNumberOfBedrooms(),
	        property.getNumberOfBathrooms(),
	        property.getFurnished(),
	        property.getPetFriendly(),
	        property.getGarage(),
	        property.getIsOwner(),
	        property.getVideoUrl(),
	        property.getPhoneNumber(),
	        property.getPhotoUrls(),
	        property.getStatus(),
	        property.getType(),
	        property.getUser().getUserId()
	    );
	}
}
