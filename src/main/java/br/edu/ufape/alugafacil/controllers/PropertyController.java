package br.edu.ufape.alugafacil.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.alugafacil.dtos.property.PropertyRequest;
import br.edu.ufape.alugafacil.dtos.property.PropertyResponse;
import br.edu.ufape.alugafacil.services.interfaces.IPropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/properties")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PropertyController {
	
	private final IPropertyService propertyService;
	
	@PostMapping
	public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyRequest request) {
		PropertyResponse response = propertyService.createProperty(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping
	public ResponseEntity<List<PropertyResponse>> listAll() {
		return ResponseEntity.ok(propertyService.getAllProperties());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PropertyResponse> getById(@PathVariable UUID id) {
		PropertyResponse response = propertyService.getPropertyById(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/owner/{userId}")
	public ResponseEntity<List<PropertyResponse>> listByOwner(@PathVariable UUID userId) { // Mude de 'id' para 'userId'
		return ResponseEntity.ok(propertyService.getPropertiesByUserId(userId));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PropertyResponse> update(@PathVariable UUID id,
			@Valid @RequestBody PropertyRequest request) {
		PropertyResponse response = propertyService.updateProperty(id, request);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		propertyService.deleteProperty(id);
		return ResponseEntity.noContent().build();
	}
}
