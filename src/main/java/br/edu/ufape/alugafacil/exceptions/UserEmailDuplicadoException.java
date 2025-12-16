package br.edu.ufape.alugafacil.exceptions;

public class UserEmailDuplicadoException extends RuntimeException {

    public UserEmailDuplicadoException() {
        super("Já existe um usuário cadastrado com esse email");
    }
    public UserEmailDuplicadoException(String message) {
        super(message);
    }
}
