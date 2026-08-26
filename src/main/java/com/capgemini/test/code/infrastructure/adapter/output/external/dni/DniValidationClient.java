package com.capgemini.test.code.infrastructure.adapter.output.external.dni;

import com.capgemini.test.code.infrastructure.adapter.output.external.dni.dto.DniValidationRequest;
import com.capgemini.test.code.infrastructure.adapter.output.external.dni.dto.DniValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * DniValidationClient - Cliente Feign para validar DNI
 *
 * Comunica con API externa en puerto 1080 (mock-server)
 * Endpoint: PATCH /check-dni
 */
@FeignClient(
    name = "dniValidation",
    url = "${external.dni-validation.url}",
    configuration = DniValidationFeignConfig.class
)
public interface DniValidationClient {

    @PatchMapping("/check-dni")
    DniValidationResponse validateDni(@RequestBody DniValidationRequest request);
}

