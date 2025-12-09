package br.edu.ufape.alugafacil.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String id) {
        super("N~so foi possível encontrar o usuário. ");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
