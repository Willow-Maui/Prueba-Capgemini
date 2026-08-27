package com.capgemini.test.code.infrastructure.exception;

import com.capgemini.test.code.domain.exceptions.DomainException;
import com.capgemini.test.code.domain.user.exceptions.DuplicateEmailException;
import com.capgemini.test.code.domain.user.exceptions.InvalidDniException;
import com.capgemini.test.code.domain.user.exceptions.InvalidEmailException;
import com.capgemini.test.code.domain.user.exceptions.InvalidPhoneException;
import com.capgemini.test.code.domain.user.exceptions.InvalidRoleException;
import com.capgemini.test.code.domain.user.exceptions.InvalidUserNameException;
import com.capgemini.test.code.domain.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para GlobalExceptionHandler.
 * Verifica que las excepciones se mapean correctamente a respuestas HTTP.
 */
@DisplayName("GlobalExceptionHandler - Manejo de Excepciones")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Nested
    @DisplayName("Excepciones de validación (409 Conflict)")
    class ValidationExceptions {

        @Test
        @DisplayName("Maneja InvalidUserNameException → 409 Conflict")
        void handleInvalidUserNameException() {
            InvalidUserNameException ex = new InvalidUserNameException("Nombre muy largo");
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleInvalidUserName(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getMessage()).contains("error validation userName");
            assertThat(response.getBody().getMessage()).contains("Nombre muy largo");
        }

        @Test
        @DisplayName("Maneja InvalidEmailException → 409 Conflict")
        void handleInvalidEmailException() {
            InvalidEmailException ex = new InvalidEmailException("Email sin @");
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleInvalidEmail(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getMessage()).contains("error validation email");
            assertThat(response.getBody().getMessage()).contains("Email sin @");
        }

        @Test
        @DisplayName("Maneja InvalidDniException → 409 Conflict")
        void handleInvalidDniException() {
            InvalidDniException ex = new InvalidDniException("DNI inválido");
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleInvalidDni(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getMessage()).contains("error validation dni");
            assertThat(response.getBody().getMessage()).contains("DNI inválido");
        }

        @Test
        @DisplayName("Maneja InvalidPhoneException → 409 Conflict")
        void handleInvalidPhoneException() {
            InvalidPhoneException ex = new InvalidPhoneException("Teléfono obligatorio");
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleInvalidPhone(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getMessage()).contains("error validation phone");
            assertThat(response.getBody().getMessage()).contains("Teléfono obligatorio");
        }

        @Test
        @DisplayName("Maneja InvalidRoleException → 409 Conflict")
        void handleInvalidRoleException() {
            InvalidRoleException ex = new InvalidRoleException("Rol inválido");
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleInvalidRole(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getMessage()).contains("error validation rol");
            assertThat(response.getBody().getMessage()).contains("Rol inválido");
        }

        @Test
        @DisplayName("Maneja DuplicateEmailException → 409 Conflict")
        void handleDuplicateEmailException() {
            DuplicateEmailException ex = new DuplicateEmailException("Email duplicado");
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleDuplicateEmail(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getMessage()).contains("error validation email");
            assertThat(response.getBody().getMessage()).contains("Email duplicado");
        }
    }

    @Nested
    @DisplayName("Excepciones de recurso no encontrado (404 Not Found)")
    class ResourceNotFoundExceptions {

        @Test
        @DisplayName("Maneja UserNotFoundException → 404 Not Found")
        void handleUserNotFoundException() {
            UserNotFoundException ex = new UserNotFoundException(999L);
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleUserNotFound(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(404);
            assertThat(response.getBody().getMessage()).contains("User not found");
            assertThat(response.getBody().getMessage()).contains("999");
        }
    }

    @Nested
    @DisplayName("Excepciones generales")
    class GeneralExceptions {

        @Test
        @DisplayName("Maneja DomainException → 409 Conflict")
        void handleDomainException() {
            // Usar una subclase concreta de DomainException
            DomainException ex = new InvalidDniException("Error de dominio");
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleDomainException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getMessage()).contains("error validation");
            assertThat(response.getBody().getMessage()).contains("Error de dominio");
        }

        @Test
        @DisplayName("Maneja Exception → 500 Internal Server Error")
        void handleGeneralException() {
            Exception ex = new RuntimeException("Error inesperado");
            ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                    handler.handleGeneralException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo(500);
            assertThat(response.getBody().getMessage()).contains("Internal server error");
            assertThat(response.getBody().getMessage()).contains("Error inesperado");
        }
    }

    @Nested
    @DisplayName("ErrorResponse DTO")
    class ErrorResponseDtoTests {

        @Test
        @DisplayName("Construye ErrorResponse con builder")
        void buildErrorResponse() {
            GlobalExceptionHandler.ErrorResponse response =
                    GlobalExceptionHandler.ErrorResponse.builder()
                            .code(409)
                            .message("Test message")
                            .build();

            assertThat(response.getCode()).isEqualTo(409);
            assertThat(response.getMessage()).isEqualTo("Test message");
        }

        @Test
        @DisplayName("Construye ErrorResponse vacío")
        void buildEmptyErrorResponse() {
            GlobalExceptionHandler.ErrorResponse response = new GlobalExceptionHandler.ErrorResponse();
            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Setea y obtiene valores")
        void setAndGetErrorResponse() {
            GlobalExceptionHandler.ErrorResponse response = new GlobalExceptionHandler.ErrorResponse();
            response.setCode(500);
            response.setMessage("Error");

            assertThat(response.getCode()).isEqualTo(500);
            assertThat(response.getMessage()).isEqualTo("Error");
        }
    }
}




