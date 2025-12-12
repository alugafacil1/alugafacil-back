package br.edu.ufape.alugafacil.exceptions;

import java.util.Arrays;
import br.edu.ufape.alugafacil.models.enums.PlanType;

public class InvalidPlanTypeException extends RuntimeException {
    public InvalidPlanTypeException(String planType) {
        super(String.format("Tipo de plano inválido: '%s'. Os valores aceitos são: %s",
                planType,
                Arrays.toString(PlanType.values())));
    }

    public InvalidPlanTypeException() {
        super(String.format("Tipo de plano inválido. Os valores aceitos são: %s",
                Arrays.toString(PlanType.values())));
    }
}
