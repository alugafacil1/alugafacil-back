package br.edu.ufape.alugafacil.exceptions;

import java.util.UUID;

public class PlanNotFoundException extends RuntimeException {
    public PlanNotFoundException(UUID planId) {
        super("Plano não encontrado com ID: " + planId);
    }
}
