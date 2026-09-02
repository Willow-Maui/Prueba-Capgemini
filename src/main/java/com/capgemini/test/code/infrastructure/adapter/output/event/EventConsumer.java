package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * EventConsumer - Consumidor de eventos desde Kafka
 *
 * Responsabilidades:
 * 1. Escuchar eventos publicados desde WriteDB
 * 2. Deserializar mensajes de Kafka
 * 3. Delegar sincronización a servicios transaccionales
 *
 * Patrón: CQRS (Command Query Responsibility Segregation)
 * Nota: No contiene @Transactional ya que delega a servicios que sí la tienen
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    private final UserEventSyncService userEventSyncService;
    private final RoomEventSyncService roomEventSyncService;
    private final ObjectMapper objectMapper;

    /**
     * Escucha y procesa eventos de usuarios
     */
    @KafkaListener(topics = "user-events", groupId = "readdb-sync-group")
    public void consumeUserEvent(@Payload String message, Acknowledgment acknowledgment) {
        try {
            log.debug("Raw message received: {}", message);

            DomainEvent event = objectMapper.readValue(message, DomainEvent.class);
            log.info("✓ Consuming user event: {} for user ID: {}",
                event.getEventType(), event.getAggregateId());

            switch (event.getEventType()) {
                case "USER_CREATED":
                    userEventSyncService.handleUserCreated(event);
                    break;
                case "USER_UPDATED":
                    userEventSyncService.handleUserUpdated(event);
                    break;
                case "USER_DELETED":
                    userEventSyncService.handleUserDeleted(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }

            log.info("✓ User event processed successfully: {}", event.getEventType());
            // Confirmar el mensaje solo después de que se procesa exitosamente
            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            log.error("✗ Error consuming user event from message: {}", message, e);
            // No confirmar para que se reintente
            throw new RuntimeException("Failed to consume user event", e);
        }
    }

    /**
     * Escucha y procesa eventos de salas
     */
    @KafkaListener(topics = "room-events", groupId = "readdb-sync-group")
    public void consumeRoomEvent(@Payload String message, Acknowledgment acknowledgment) {
        try {
            log.debug("Raw message received: {}", message);

            DomainEvent event = objectMapper.readValue(message, DomainEvent.class);
            log.info("✓ Consuming room event: {} for room ID: {}",
                event.getEventType(), event.getAggregateId());

            switch (event.getEventType()) {
                case "ROOM_CREATED":
                    roomEventSyncService.handleRoomCreated(event);
                    break;
                case "ROOM_UPDATED":
                    roomEventSyncService.handleRoomUpdated(event);
                    break;
                case "ROOM_DELETED":
                    roomEventSyncService.handleRoomDeleted(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }

            log.info("✓ Room event processed successfully: {}", event.getEventType());
            // Confirmar el mensaje solo después de que se procesa exitosamente
            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            log.error("✗ Error consuming room event from message: {}", message, e);
            throw new RuntimeException("Failed to consume room event", e);
        }
    }

}


