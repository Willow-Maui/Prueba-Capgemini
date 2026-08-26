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
     * Lanza InvalidDniException si el DNI no es válido
     *
     * Feign realiza 3 reintentos automáticos en caso de fallo
     */
    @Override
    public void validate(String dni) throws InvalidDniException {
        try {
            log.debug("Validating DNI: {}", dni);

            DniValidationRequest request = DniValidationRequest.builder()
                .dni(dni)
                .build();

            DniValidationResponse response = client.validateDni(request);

            if (response.getValid() == null || !response.getValid()) {
                log.warn("Invalid DNI received: {}", dni);
                throw new InvalidDniException("Invalid DNI: " + dni);
            }

            log.debug("DNI validation successful: {}", dni);

        } catch (FeignException.Conflict e) {
            // HTTP 409: DNI conflict
            log.warn("DNI validation conflict: {}", dni);
            throw new InvalidDniException("DNI already exists or is invalid: " + dni);

        } catch (FeignException.ServiceUnavailable e) {
            // HTTP 503: Service unavailable
            log.error("DNI validation service unavailable", e);
            throw new InvalidDniException("DNI validation service unavailable");

        } catch (FeignException e) {
            // Otros errores Feign
            log.error("Error calling DNI validation service", e);
            throw new InvalidDniException("DNI validation service error: " + e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error validating DNI", e);
            throw new InvalidDniException("Unexpected error validating DNI");
        }
    }
}

