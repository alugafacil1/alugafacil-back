package br.edu.ufape.alugafacil.dtos.property;

import java.util.List;

import br.edu.ufape.alugafacil.dtos.simpleProperty.SimplePropertyResponse;
import org.springframework.data.domain.Page;

public record CombinedPropertiesResponse(
    Page<PropertyResponse> properties,
    List<SimplePropertyResponse> simpleProperties
) {}
