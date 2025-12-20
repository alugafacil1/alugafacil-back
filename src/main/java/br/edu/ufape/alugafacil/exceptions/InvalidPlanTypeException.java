package br.edu.ufape.alugafacil.exceptions;

import java.util.Arrays;
import br.edu.ufape.alugafacil.enums.UserType;

public class InvalidPlanTypeException extends RuntimeException {
    
    public InvalidPlanTypeException(String invalidValue) {
        super(String.format("Público alvo inválido: '%s'. Os valores aceitos são: %s",
                invalidValue,
                Arrays.toString(UserType.values())));
    }

    public InvalidPlanTypeException() {
        super(String.format("Público alvo é obrigatório. Os valores aceitos são: %s",
                Arrays.toString(UserType.values())));
    }
}