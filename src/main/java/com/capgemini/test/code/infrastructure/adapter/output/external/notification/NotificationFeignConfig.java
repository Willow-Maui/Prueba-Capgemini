package com.capgemini.test.code.infrastructure.adapter.output.external.notification;

import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * NotificationFeignConfig - Configuración de Feign para Notification
 *
 * Configura:
 * - Retry: 2 intentos con backoff exponencial
 * - Timeout: 5s
 * - Logger level: FULL
 */
@Configuration
@Slf4j
public class NotificationFeignConfig {

    @Bean(name = "notificationRetryer")
    public Retryer retryer() {
        // Retry: initial interval 100ms, max interval 1000ms, 2 attempts
        return new Retryer.Default(100, 1000, 2);
    }

    @Bean(name = "notificationLoggerLevel")
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
}

