package com.capgemini.test.code.infrastructure.adapter.output.external.dni;

import com.capgemini.test.code.application.ports.output.DniValidationPort;
import com.capgemini.test.code.domain.user.exceptions.InvalidDniException;
import com.capgemini.test.code.infrastructure.adapter.output.external.dni.dto.DniValidationRequest;
import com.capgemini.test.code.infrastructure.adapter.output.external.dni.dto.DniValidationResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * DniValidationAdapter - Implementa DniValidationPort
 *
 * Responsabilidades:
 * 1. Llamar a API externa de validación de DNI
 * 2. Mapear respuesta a excepciones de dominio
 * 3. Manejo de retries automático (Feign)
 * 4. Manejo de excepciones Feign
 *
 * Patrón: Adapter (infraestructura)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DniValidationAdapter implements DniValidationPort {

    private final DniValidationClient client;

    /**
     * Valida DNI contra API externa
     *
     * La validez se determina ÚNICAMENTE por el status HTTP (según README):
     * - HTTP 200: DNI es válido
     * - HTTP 409: DNI es inválido
     * - Otros: Error del servicio
     *
     * NO se usa el campo "valid" de la respuesta (puede no existir).
     * Feign realiza 3 reintentos automáticos en caso de fallo
     */
    @Override
    public void validate(String dni) throws InvalidDniException {
        try {
            log.debug("Validating DNI: {}", dni);

            DniValidationRequest request = DniValidationRequest.builder()
                .dni(dni)
                .build();

            // Si la llamada llega aquí sin excepción, significa HTTP 200 (válido)
            DniValidationResponse response = client.validateDni(request);

            log.info("✓ DNI validation successful (HTTP 200): {}", dni);
            log.debug("Response: {}", response);

        } catch (FeignException.Conflict e) {
            // HTTP 409: DNI conflict/invalid
            log.info("✗ DNI validation failed (HTTP 409 Conflict): {}", dni);
            throw new InvalidDniException("Invalid DNI: " + dni);

        } catch (FeignException.ServiceUnavailable e) {
            // HTTP 503: Service unavailable
            log.error("DNI validation service unavailable (HTTP 503)", e);
            throw new InvalidDniException("DNI validation service unavailable");

        } catch (FeignException.BadRequest e) {
            // HTTP 400: Bad request (formato inválido del DNI)
            log.warn("DNI validation bad request (HTTP 400): {}", dni);
            throw new InvalidDniException("Invalid DNI format: " + dni);

        } catch (FeignException e) {
            // Otros errores Feign (4xx, 5xx)
            int status = e.status();
            log.error("DNI validation service error (HTTP {}): {}", status, e.getMessage());
            throw new InvalidDniException("DNI validation service error");

        } catch (InvalidDniException e) {
            // Re-lanzar InvalidDniException sin envolverla nuevamente
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error validating DNI: {}", e.getMessage(), e);
            throw new InvalidDniException("Unexpected error validating DNI");
        }
    }
}

