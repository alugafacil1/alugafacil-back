package br.edu.ufape.alugafacil.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.edu.ufape.alugafacil.dtos.property.PropertyRequest;
import br.edu.ufape.alugafacil.dtos.property.PropertyResponse;
import br.edu.ufape.alugafacil.mappers.PropertyMapper;
import br.edu.ufape.alugafacil.models.Property;
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
	public List<PropertyResponse> getAllProperties() {
		return propertyRepository.findAll().stream()
				.map(propertyMapper::toResponse)
				.collect(Collectors.toList());
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
