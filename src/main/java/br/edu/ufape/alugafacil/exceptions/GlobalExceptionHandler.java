package br.edu.ufape.alugafacil.exceptions;

import br.edu.ufape.alugafacil.dtos.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(KeycloakAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakAuthenticationException(KeycloakAuthenticationException ex) {
        logger.warn("Falha de autenticação no Keycloak: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "Falha de autenticação",
                ex.getMessage(),
                Arrays.asList(ex.getStackTrace()),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePlanNotFoundException(
            PlanNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicatePlanNameException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicatePlanNameException(
            DuplicatePlanNameException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidPlanTypeException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPlanTypeException(
            InvalidPlanTypeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Erro de validação nos campos fornecidos",
                request.getRequestURI(),
                details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        String message = "Erro ao processar o corpo da requisição";
        List<String> details = new ArrayList<>();

        if (ex.getMessage() != null && ex.getMessage().contains("PlanType")) {
            message = "Tipo de plano inválido fornecido";
            details.add("Os valores aceitos para planType são: TENANT, OWNER, REALTOR");
        } else {
            details.add(ex.getMessage());
        }

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message,
                request.getRequestURI(),
                details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Business Error", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex, HttpServletRequest request) {
        ex.printStackTrace(); 
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Ocorreu um erro inesperado no servidor", request);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(
            HttpStatus status, String errorTitle, String message, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                status.value(),
                errorTitle,
                message,
                request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}