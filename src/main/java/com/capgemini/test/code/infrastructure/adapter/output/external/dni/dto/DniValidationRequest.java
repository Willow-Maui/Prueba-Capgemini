package com.capgemini.test.code.infrastructure.adapter.output.external.dni.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DniValidationRequest - DTO para solicitud a API de validación de DNI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DniValidationRequest {

    @JsonProperty("dni")
    private String dni;
}

