package com.capgemini.test.code.infrastructure.exception;

import com.capgemini.test.code.domain.exceptions.DomainException;
import com.capgemini.test.code.domain.user.exceptions.DuplicateEmailException;
import com.capgemini.test.code.domain.user.exceptions.InvalidDniException;
import com.capgemini.test.code.domain.user.exceptions.InvalidEmailException;
import com.capgemini.test.code.domain.user.exceptions.InvalidPhoneException;
import com.capgemini.test.code.domain.user.exceptions.InvalidRoleException;
import com.capgemini.test.code.domain.user.exceptions.InvalidUserNameException;
import com.capgemini.test.code.domain.user.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler para convertir excepciones de dominio en respuestas HTTP.
 *
 * Responsabilidad:
 * - Capturar excepciones del dominio (DomainException y subclases)
 * - Convertir a respuestas HTTP con código y mensaje
 * - Retornar 409 Conflict para errores de validación
 * - Retornar 404 Not Found para recursos no encontrados
 *
 * Patrón: Centralized Exception Handling (Spring @RestControllerAdvice)
 *
 * Excepciones manejadas:
 * - InvalidUserNameException → 409 (nombre inválido)
 * - InvalidEmailException → 409 (email inválido)
 * - InvalidDniException → 409 (DNI inválido)
 * - InvalidPhoneException → 409 (phone inválido)
 * - InvalidRoleException → 409 (rol inválido)
 * - DuplicateEmailException → 409 (email duplicado)
 * - UserNotFoundException → 404 (usuario no encontrado)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * DTO para respuesta de error HTTP.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        /** Código HTTP del error */
        private int code;
        /** Mensaje descriptivo del error */
        private String message;
    }

    /**
     * Maneja InvalidUserNameException → 409 Conflict
     */
    @ExceptionHandler(InvalidUserNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserName(InvalidUserNameException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .code(409)
                .message("error validation userName: " + ex.getMessage())
                .build());
    }

    /**
     * Maneja InvalidEmailException → 409 Conflict
     */
    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmail(InvalidEmailException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .code(409)
                .message("error validation email: " + ex.getMessage())
                .build());
    }

    /**
     * Maneja InvalidDniException → 409 Conflict
     */
    @ExceptionHandler(InvalidDniException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDni(InvalidDniException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .code(409)
                .message("error validation dni: " + ex.getMessage())
                .build());
    }

    /**
     * Maneja InvalidPhoneException → 409 Conflict
     */
    @ExceptionHandler(InvalidPhoneException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPhone(InvalidPhoneException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .code(409)
                .message("error validation phone: " + ex.getMessage())
                .build());
    }

    /**
     * Maneja InvalidRoleException → 409 Conflict
     */
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRole(InvalidRoleException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .code(409)
                .message("error validation rol: " + ex.getMessage())
                .build());
    }

    /**
     * Maneja DuplicateEmailException → 409 Conflict
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .code(409)
                .message("error validation email: " + ex.getMessage())
                .build());
    }

    /**
     * Maneja UserNotFoundException → 404 Not Found
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder()
                .code(404)
                .message("User not found: " + ex.getMessage())
                .build());
    }

    /**
     * Maneja cualquier otra DomainException → 409 Conflict
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse.builder()
                .code(409)
                .message("error validation: " + ex.getMessage())
                .build());
    }

    /**
     * Maneja excepciones generales → 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder()
                .code(500)
                .message("Internal server error: " + ex.getMessage())
                .build());
    }
}

