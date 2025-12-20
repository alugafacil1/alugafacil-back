package br.edu.ufape.alugafacil.exceptions;

public class UserPreferenceNotFound extends RuntimeException {
    public UserPreferenceNotFound(String message) {
        super(message);
    }
    public UserPreferenceNotFound() {super("Não foi possível encontrar a preferência do usuário.");}
}
