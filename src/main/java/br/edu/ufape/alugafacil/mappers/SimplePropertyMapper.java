package br.edu.ufape.alugafacil.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.edu.ufape.alugafacil.dtos.simpleProperty.SimplePropertyRequest;
import br.edu.ufape.alugafacil.dtos.simpleProperty.SimplePropertyResponse;
import br.edu.ufape.alugafacil.models.SimpleProperty;

@Mapper(componentModel = "spring")
public interface SimplePropertyMapper {
    SimplePropertyResponse toResponse(SimpleProperty simpleProperty);
    
    @Mapping(target = "id", ignore = true)
    SimpleProperty toEntity(SimplePropertyRequest request);
}
