package com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

/**
 * EventEntity - Event Store (auditoría + sincronización)
 * Almacena todos los eventos de dominio ocurridos en WriteDB (PostgreSQL)
 * Usada para CQRS y replicación a ReadDB (MySQL) a través de Kafka
 */
@Entity
@Table(name = "events", indexes = {
    @Index(name = "idx_events_aggregate", columnList = "aggregate_type,aggregate_id"),
    @Index(name = "idx_events_published", columnList = "published")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "payload", nullable = false, columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "published", nullable = false)
    private Boolean published;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (published == null) {
            published = false;
        }
    }
}


