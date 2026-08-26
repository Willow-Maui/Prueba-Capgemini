package com.capgemini.test.code.infrastructure.adapter.output.external.dni.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DniValidationResponse - DTO para respuesta de API de validación de DNI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DniValidationResponse {

    @JsonProperty("valid")
    private Boolean valid;

    @JsonProperty("message")
    private String message;
}

