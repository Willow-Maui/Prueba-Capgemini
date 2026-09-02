package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.capgemini.test.code.application.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.capgemini.test.code.domain.room.model.Room;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.event.EventEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.event.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * EventPublisher - Publicador de eventos de dominio
 *
 * Responsabilidades:
 * 1. Persistir eventos en Event Store (WriteDB)
 * 2. Publicar eventos a Kafka para sincronización con ReadDB
 *
 * Patrón: Transactional Outbox
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final EventJpaRepository eventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC_USER_EVENTS = "user-events";
    private static final String TOPIC_ROOM_EVENTS = "room-events";

    /**
     * Publica evento de creación de usuario
     * Guarda en Event Store y envía a Kafka con clave (aggregate_id)
     */
    public void publishUserCreated(UserDTO user) {
        try {
            // Convertir a JSON string
            String jsonPayload = objectMapper.writeValueAsString(user);

            // Guardar en Event Store (payload como JSONB)
            EventEntity event = EventEntity.builder()
                .eventType("USER_CREATED")
                .aggregateType("USER")
                .aggregateId(user.getId())
                .payload(jsonPayload)  // Se guarda como JSONB en PostgreSQL
                .published(false)
                .build();

            EventEntity saved = eventRepository.save(event);
            log.info("✓ Event saved in Event Store: USER_CREATED for user ID: {}", user.getId());

            // Publicar a Kafka (NO-BLOCKING - si falla, continúa)
            try {
                String eventMessage = objectMapper.writeValueAsString(
                    DomainEvent.builder()
                        .eventId(saved.getId())
                        .eventType("USER_CREATED")
                        .aggregateId(user.getId())
                        .aggregateType("USER")
                        .payload(jsonPayload)
                        .timestamp(saved.getCreatedAt())
                        .build()
                );

                // Enviar con clave = aggregateId para tópicos compactados
                String key = String.valueOf(user.getId());
                kafkaTemplate.send(TOPIC_USER_EVENTS, key, eventMessage);

                // Marcar como publicado solo si fue exitoso
                saved.setPublished(true);
                eventRepository.save(saved);

                log.info("✓ Event published to Kafka: USER_CREATED for user ID: {}", user.getId());
            } catch (Exception kafkaEx) {
                // Si Kafka falla, no es crítico - solo registramos como no publicado
                log.warn("⚠ Failed to publish USER_CREATED event to Kafka for user ID: {}. Event saved in store.",
                    user.getId(), kafkaEx);
            }
        } catch (Exception e) {
            log.error("✗ Error publishing USER_CREATED event", e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }

    /**
     * Publica evento de creación de sala
     * Guarda en Event Store y envía a Kafka con clave (aggregate_id)
     */
    public void publishRoomCreated(Room room) {
        try {
            // Convertir a JSON string
            String jsonPayload = objectMapper.writeValueAsString(room);

            // Guardar en Event Store (payload como JSONB)
            EventEntity event = EventEntity.builder()
                .eventType("ROOM_CREATED")
                .aggregateType("ROOM")
                .aggregateId(room.getId())
                .payload(jsonPayload)  // Se guarda como JSONB en PostgreSQL
                .published(false)
                .build();

            EventEntity saved = eventRepository.save(event);
            log.info("✓ Event saved in Event Store: ROOM_CREATED for room ID: {}", room.getId());

            // Publicar a Kafka (NO-BLOCKING)
            try {
                String eventMessage = objectMapper.writeValueAsString(
                    DomainEvent.builder()
                        .eventId(saved.getId())
                        .eventType("ROOM_CREATED")
                        .aggregateId(room.getId())
                        .aggregateType("ROOM")
                        .payload(jsonPayload)
                        .timestamp(saved.getCreatedAt())
                        .build()
                );

                // Enviar con clave = aggregateId para tópicos compactados
                String key = String.valueOf(room.getId());
                kafkaTemplate.send(TOPIC_ROOM_EVENTS, key, eventMessage);

                saved.setPublished(true);
                eventRepository.save(saved);

                log.info("✓ Event published to Kafka: ROOM_CREATED for room ID: {}", room.getId());
            } catch (Exception kafkaEx) {
                // Si Kafka falla, no es crítico
                log.warn("⚠ Failed to publish ROOM_CREATED event to Kafka for room ID: {}. Event saved in store.",
                    room.getId(), kafkaEx);
            }
        } catch (Exception e) {
            log.error("✗ Error publishing ROOM_CREATED event", e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}

