package br.edu.ufape.alugafacil.exceptions;

public class PlanNotFoundException extends RuntimeException {
    public PlanNotFoundException(String planId) {
        super("Plano não encontrado com ID: " + planId);
    }
}
