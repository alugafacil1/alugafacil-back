package br.edu.ufape.alugafacil.dtos.property;

import br.edu.ufape.alugafacil.enums.PropertyStatus;

public record PropertyStatusDTO(PropertyStatus status, String reason) {}
