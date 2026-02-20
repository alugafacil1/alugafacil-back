package br.edu.ufape.alugafacil.config;
import org.springframework.http.HttpStatus;
import br.edu.ufape.alugafacil.exceptions.ResourceNotFoundException;
import br.edu.ufape.alugafacil.exceptions.StandardError;
import br.edu.ufape.alugafacil.exceptions.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;

@ControllerAdvice
@Slf4j
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
        err.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value()); 
        err.setError("Erro de Validação");
        err.setMessage("Ocorreram erros na validação dos campos");
        err.setPath(request.getRequestURI());

        
        log.error(" Erro de validação detectado em: {}", request.getRequestURI());

        e.getBindingResult().getFieldErrors().forEach(f -> {
            
            log.error("[VALIDATION ERROR] Campo: {} | Mensagem: {} | Valor rejeitado: [{}]", 
                    f.getField(), f.getDefaultMessage(), f.getRejectedValue());
            
            err.addError(f.getField(), f.getDefaultMessage());
        });
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }
}