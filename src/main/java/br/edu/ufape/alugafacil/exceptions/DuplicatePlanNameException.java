package br.edu.ufape.alugafacil.exceptions;

public class DuplicatePlanNameException extends RuntimeException {
    public DuplicatePlanNameException(String planName) {
        super("Já existe um plano com o nome: " + planName);
    }
}
