package com.capgemini.test.code.infrastructure.adapter.output.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DomainEvent - DTO para eventos que se publican en Kafka
 * Contenedor que encapsula el evento de dominio con metadatos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainEvent {
    private Long eventId;
    private String eventType;
    private Long aggregateId;
    private String aggregateType;
    private String payload;
    private LocalDateTime timestamp;
}

