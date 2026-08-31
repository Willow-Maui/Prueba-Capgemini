package com.capgemini.test.code.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedKafkaFallbackConfig {

    @Bean(name = "embeddedKafka")
    @ConditionalOnProperty(
            name = "spring.kafka.embedded.enabled",
            havingValue = "false",
            matchIfMissing = true
    )
    public Object embeddedKafka() {
        return new Object();
    }
}