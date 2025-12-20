package br.edu.ufape.alugafacil.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Não foi possível encontrar o usuário. ");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
