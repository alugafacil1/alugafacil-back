package br.edu.ufape.alugafacil.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.edu.ufape.alugafacil.dtos.property.PropertyFilterRequest;
import br.edu.ufape.alugafacil.dtos.property.PropertyRequest;
import br.edu.ufape.alugafacil.dtos.property.PropertyResponse;
import br.edu.ufape.alugafacil.mappers.PropertyMapper;
import br.edu.ufape.alugafacil.models.Property;
import br.edu.ufape.alugafacil.models.QProperty;
import br.edu.ufape.alugafacil.models.enums.PropertyStatus;
import br.edu.ufape.alugafacil.repositories.PropertyRepository;
import br.edu.ufape.alugafacil.services.interfaces.IPropertyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService implements IPropertyService {
	
	private final PropertyRepository propertyRepository;
//	private final UserRepository userRepository;
	private final PropertyMapper propertyMapper;
	
	@Override
	@Transactional
	public PropertyResponse createProperty(PropertyRequest request) {
		
//		 User owner = userRepository.findById(request.userId())
//		 		.orElseThrow(() -> new RuntimeException("Usuàrio nâo encontrado com ID: " + request.userId()));
//		
		 Property property = propertyMapper.toEntity(request);
		 
//		 property.setUser(owner);
		 
		
		 return propertyMapper.toResponse(propertyRepository.save(property));
	}

	@Override
	public PropertyResponse getPropertyById(UUID id) {
		Property property = propertyRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
		return propertyMapper.toResponse(property);
	}

	@Override
	public Page<PropertyResponse> getAllProperties(PropertyFilterRequest filters, Pageable pageable) {
		
		QProperty qProperty = QProperty.property;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		if (filters != null) {
            if (filters.getMinPrice() != null) {
                builder.and(qProperty.priceInCents.goe(filters.getMinPrice() * 100));
            }
            if (filters.getMaxPrice() != null) {
                builder.and(qProperty.priceInCents.loe(filters.getMaxPrice() * 100));
            }

            if (filters.getMinRooms() != null) {
                builder.and(qProperty.numberOfRooms.goe(filters.getMinRooms()));
            }
            if (filters.getMinBedrooms() != null) {
                builder.and(qProperty.numberOfBedrooms.goe(filters.getMinBedrooms()));
            }

            if (filters.getFurnished() != null) {
                builder.and(qProperty.furnished.eq(filters.getFurnished()));
            }
            if (filters.getPetFriendly() != null) {
                builder.and(qProperty.petFriendly.eq(filters.getPetFriendly()));
            }
            
            if (filters.getType() != null) {
                builder.and(qProperty.type.eq(filters.getType()));
            }
            if (filters.getStatus() != null) {
                builder.and(qProperty.status.eq(filters.getStatus()));
            } else {
                 builder.and(qProperty.status.eq(PropertyStatus.ACTIVE));
            }

            // containsIgnoreCase = ILIKE '%valor%'
            if (filters.getCity() != null && !filters.getCity().isEmpty()) {
                builder.and(qProperty.address.city.containsIgnoreCase(filters.getCity()));
            }
            if (filters.getNeighborhood() != null && !filters.getNeighborhood().isEmpty()) {
                builder.and(qProperty.address.neighborhood.containsIgnoreCase(filters.getNeighborhood()));
            }
        }
		
		Page<Property> pageResult = propertyRepository.findAll((Predicate) builder, pageable);
		
		return pageResult.map(propertyMapper::toResponse);
	}

	@Override
	public List<PropertyResponse> getPropertiesByUserId(UUID userId) {
		return propertyRepository.findByUserUserId(userId).stream()
				.map(propertyMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public PropertyResponse updateProperty(UUID id, PropertyRequest request) {
		Property property = propertyRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
		propertyMapper.updateEntityFromDto(request, property);
		
		return propertyMapper.toResponse(propertyRepository.save(property));
	}

	@Override
	public void deleteProperty(UUID id) {
		Property property = propertyRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
		
		propertyRepository.deleteById(id);
		
	}
}
