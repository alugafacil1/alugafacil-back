package br.edu.ufape.alugafacil.services.interfaces;

import java.util.List;
import java.util.UUID;

import br.edu.ufape.alugafacil.dtos.property.PropertyRequest;
import br.edu.ufape.alugafacil.dtos.property.PropertyResponse;
import br.edu.ufape.alugafacil.models.Property;

public interface IPropertyService {
	PropertyResponse createProperty(PropertyRequest request);
	PropertyResponse getPropertyById(UUID id);
    List<PropertyResponse> getAllProperties();
    List<PropertyResponse> getPropertiesByUserId(UUID userId);
    PropertyResponse updateProperty(UUID id, PropertyRequest request);
    void deleteProperty(UUID id);
}
