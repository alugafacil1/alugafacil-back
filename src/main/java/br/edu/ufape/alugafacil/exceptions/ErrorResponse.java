package br.edu.ufape.alugafacil.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter @NoArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private List<StackTraceElement> stackTrace;
    private LocalDateTime timestamp;

    public ErrorResponse(String exceptionType, String message, List<StackTraceElement> stackTrace, LocalDateTime timestamp) {
        this.error = exceptionType;
        this.message = message;
        this.stackTrace = stackTrace;
        this.timestamp = timestamp;
    }
}
