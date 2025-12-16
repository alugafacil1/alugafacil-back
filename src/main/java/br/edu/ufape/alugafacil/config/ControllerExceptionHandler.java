package br.edu.ufape.alugafacil.config;

import br.edu.ufape.alugafacil.exceptions.ResourceNotFoundException;
import br.edu.ufape.alugafacil.exceptions.StandardError;
import br.edu.ufape.alugafacil.exceptions.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Recurso não encontrado";
        HttpStatusCode status = HttpStatusCode.valueOf(404);
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatusCode.valueOf(422).value());
        err.setError("Erro de Validação");
        err.setMessage("Ocorreram erros na validação dos campos");
        err.setPath(request.getRequestURI());

        e.getBindingResult().getFieldErrors().forEach(f -> {
            err.addError(f.getField(), f.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatusCode.valueOf(422)).body(err);
    }
}