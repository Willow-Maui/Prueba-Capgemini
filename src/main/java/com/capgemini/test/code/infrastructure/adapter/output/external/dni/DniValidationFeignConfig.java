package com.capgemini.test.code.infrastructure.adapter.output.external.dni;

import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DniValidationFeignConfig - Configuración de Feign para DNI Validation
 *
 * Configura:
 * - Retry: 3 intentos con backoff exponencial
 * - Timeout: 5s
 * - Logger level: FULL
 */
@Configuration
@Slf4j
public class DniValidationFeignConfig {

    @Bean(name = "dniRetryer")
    public Retryer retryer() {
        // Retry: initial interval 100ms, max interval 1000ms, 3 attempts
        return new Retryer.Default(100, 1000, 3);
    }

    @Bean(name = "dniLoggerLevel")
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
}

