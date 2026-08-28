package com.capgemini.test.code.infrastructure.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;

import java.util.HashMap;
import java.util.Map;

/**
 * EmbeddedKafkaConfig - Configuración de Kafka embebido
 *
 * Se activa solo cuando se ejecuta con el perfil "local"
 * Proporciona un broker Kafka dentro del proceso de la aplicación
 *
 * Uso: mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
 */
@Configuration
@ConditionalOnProperty(name = "kafka.embedded.enabled", havingValue = "true")
@Slf4j
public class EmbeddedKafkaConfig {

    @Bean
    public EmbeddedKafkaBroker embeddedKafka() {
        log.info("🚀 Iniciando Kafka embebido...");

        EmbeddedKafkaBroker kafkaBroker = new EmbeddedKafkaKraftBroker(
            1,                              // 1 broker
            1,                           // controlledShutdown
            "user-events", "room-events"   // tópicos
        );

        Map<String, String> props = new HashMap<>();

        props.put("auto.create.topics.enable", "true");
        props.put("num.network.threads", "8");
        props.put("num.io.threads", "8");

        kafkaBroker.brokerProperties(props);

        kafkaBroker.kafkaPorts(19092);

        kafkaBroker.afterPropertiesSet();

        log.info("✅ Kafka embebido iniciado en puerto: {}", kafkaBroker.getBrokersAsString());
        return kafkaBroker;
    }

    @PostConstruct
    public void init() {
        log.info("EMBEDDED KAFKA CONFIG CARGADA");
    }
}

