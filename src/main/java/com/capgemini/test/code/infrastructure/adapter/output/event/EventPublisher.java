package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.room.model.Room;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.event.EventEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.event.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

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
     * Guarda en Event Store y envía a Kafka
     */
    public void publishUserCreated(User user) {
        try {
            String payload = objectMapper.writeValueAsString(user);

            // Guardar en Event Store
            EventEntity event = EventEntity.builder()
                .eventType("USER_CREATED")
                .aggregateType("USER")
                .aggregateId(user.getId())
                .payload(payload)
                .createdAt(LocalDateTime.now())
                .published(false)
                .build();

            EventEntity saved = eventRepository.save(event);

            // Publicar a Kafka
            kafkaTemplate.send(TOPIC_USER_EVENTS, objectMapper.writeValueAsString(
                DomainEvent.builder()
                    .eventId(saved.getId())
                    .eventType("USER_CREATED")
                    .aggregateId(user.getId())
                    .aggregateType("USER")
                    .payload(payload)
                    .timestamp(saved.getCreatedAt())
                    .build()
            ));

            // Marcar como publicado
            saved.setPublished(true);
            eventRepository.save(saved);

            log.info("Event published: USER_CREATED for user ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Error publishing USER_CREATED event", e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }

    /**
     * Publica evento de creación de sala
     */
    public void publishRoomCreated(Room room) {
        try {
            String payload = objectMapper.writeValueAsString(room);

            EventEntity event = EventEntity.builder()
                .eventType("ROOM_CREATED")
                .aggregateType("ROOM")
                .aggregateId(room.getId())
                .payload(payload)
                .createdAt(LocalDateTime.now())
                .published(false)
                .build();

            EventEntity saved = eventRepository.save(event);

            kafkaTemplate.send(TOPIC_ROOM_EVENTS, objectMapper.writeValueAsString(
                DomainEvent.builder()
                    .eventId(saved.getId())
                    .eventType("ROOM_CREATED")
                    .aggregateId(room.getId())
                    .aggregateType("ROOM")
                    .payload(payload)
                    .timestamp(saved.getCreatedAt())
                    .build()
            ));

            saved.setPublished(true);
            eventRepository.save(saved);

            log.info("Event published: ROOM_CREATED for room ID: {}", room.getId());
        } catch (Exception e) {
            log.error("Error publishing ROOM_CREATED event", e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}

