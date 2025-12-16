package br.edu.ufape.alugafacil.exceptions;

public class UserCpfDuplicadoException extends Exception{
    public UserCpfDuplicadoException() {
        super("Já existe um usuário com este cpf.");
    }

    public UserCpfDuplicadoException(String message) {
        super(message);
    }
}
