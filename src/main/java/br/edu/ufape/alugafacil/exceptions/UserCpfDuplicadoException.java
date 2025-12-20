package br.edu.ufape.alugafacil.exceptions;

public class UserCpfDuplicadoException extends RuntimeException {
    public UserCpfDuplicadoException() {
        super("CPF já cadastrado no sistema.");
    }
}
