package com.capgemini.test.code.infrastructure.adapter.output.external.dni.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DniValidationResponse - DTO para respuesta de API de validación de DNI
 *
 * Nota: La validez se determina ÚNICAMENTE por el status HTTP (según README),
 * no por campos de la respuesta.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DniValidationResponse {


    @JsonProperty("message")
    private String message;
}



