package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.room.model.Room;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.mapper.UserMapper;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.mapper.RoomMapper;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.user.UserEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.room.RoomEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user.UserReadEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.room.RoomReadEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user.UserReadJpaRepository;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.room.RoomReadJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * EventConsumer - Consumidor de eventos desde Kafka
 *
 * Responsabilidades:
 * 1. Escuchar eventos publicados desde WriteDB
 * 2. Sincronizar ReadDB con los eventos
 * 3. Garantizar consistencia eventual
 *
 * Patrón: CQRS (Command Query Responsibility Segregation)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    private final UserReadJpaRepository userReadRepository;
    private final RoomReadJpaRepository roomReadRepository;
    private final UserMapper userMapper;
    private final RoomMapper roomMapper;
    private final ObjectMapper objectMapper;

    /**
     * Escucha y procesa eventos de usuarios
     */
    @KafkaListener(topics = "user-events", groupId = "readdb-sync-group")
    public void consumeUserEvent(String message) {
        try {
            DomainEvent event = objectMapper.readValue(message, DomainEvent.class);

            log.info("Consuming user event: {} for user ID: {}",
                event.getEventType(), event.getAggregateId());

            switch (event.getEventType()) {
                case "USER_CREATED":
                    handleUserCreated(event);
                    break;
                case "USER_UPDATED":
                    handleUserUpdated(event);
                    break;
                case "USER_DELETED":
                    handleUserDeleted(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }

            log.info("User event processed successfully: {}", event.getEventType());
        } catch (Exception e) {
            log.error("Error consuming user event", e);
            // La excepción hace que Kafka reintente el mensaje
            throw new RuntimeException("Failed to consume user event", e);
        }
    }

    /**
     * Escucha y procesa eventos de salas
     */
    @KafkaListener(topics = "room-events", groupId = "readdb-sync-group")
    public void consumeRoomEvent(String message) {
        try {
            DomainEvent event = objectMapper.readValue(message, DomainEvent.class);

            log.info("Consuming room event: {} for room ID: {}",
                event.getEventType(), event.getAggregateId());

            switch (event.getEventType()) {
                case "ROOM_CREATED":
                    handleRoomCreated(event);
                    break;
                case "ROOM_UPDATED":
                    handleRoomUpdated(event);
                    break;
                case "ROOM_DELETED":
                    handleRoomDeleted(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }

            log.info("Room event processed successfully: {}", event.getEventType());
        } catch (Exception e) {
            log.error("Error consuming room event", e);
            throw new RuntimeException("Failed to consume room event", e);
        }
    }

    private void handleUserCreated(DomainEvent event) {
        try {
            User user = objectMapper.readValue(event.getPayload(), User.class);

            UserReadEntity readEntity = UserReadEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .dni(user.getDni())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .roomId(user.getRoomId())
                .createdAt(event.getTimestamp())
                .lastSyncAt(LocalDateTime.now())
                .build();

            userReadRepository.save(readEntity);
            log.debug("User synchronized to ReadDB: {}", user.getId());
        } catch (Exception e) {
            log.error("Error handling user created event", e);
            throw new RuntimeException("Failed to handle user created event", e);
        }
    }

    private void handleUserUpdated(DomainEvent event) {
        try {
            User user = objectMapper.readValue(event.getPayload(), User.class);

            userReadRepository.findById(user.getId()).ifPresentOrElse(
                existing -> {
                    existing.setName(user.getName());
                    existing.setEmail(user.getEmail());
                    existing.setDni(user.getDni());
                    existing.setPhone(user.getPhone());
                    existing.setRole(user.getRole().name());
                    existing.setRoomId(user.getRoomId());
                    userReadRepository.save(existing);
                    log.debug("User updated in ReadDB: {}", user.getId());
                },
                () -> {
                    log.warn("User not found in ReadDB for update: {}", user.getId());
                    handleUserCreated(event);
                }
            );
        } catch (Exception e) {
            log.error("Error handling user updated event", e);
            throw new RuntimeException("Failed to handle user updated event", e);
        }
    }

    private void handleUserDeleted(DomainEvent event) {
        userReadRepository.deleteById(event.getAggregateId());
        log.debug("User deleted from ReadDB: {}", event.getAggregateId());
    }

    // ==================== Room Event Handlers ====================

    private void handleRoomCreated(DomainEvent event) {
        try {
            Room room = objectMapper.readValue(event.getPayload(), Room.class);

            RoomReadEntity readEntity = RoomReadEntity.builder()
                .id(room.getId())
                .name(room.getName())
                .createdAt(event.getTimestamp())
                .lastSyncAt(LocalDateTime.now())
                .build();

            roomReadRepository.save(readEntity);
            log.debug("Room synchronized to ReadDB: {}", room.getId());
        } catch (Exception e) {
            log.error("Error handling room created event", e);
            throw new RuntimeException("Failed to handle room created event", e);
        }
    }

    private void handleRoomUpdated(DomainEvent event) {
        try {
            Room room = objectMapper.readValue(event.getPayload(), Room.class);

            roomReadRepository.findById(event.getAggregateId()).ifPresentOrElse(
                existing -> {
                    existing.setName(room.getName());
                    roomReadRepository.save(existing);
                    log.debug("Room updated in ReadDB: {}", room.getId());
                },
                () -> {
                    log.warn("Room not found in ReadDB for update: {}", room.getId());
                    handleRoomCreated(event);
                }
            );
        } catch (Exception e) {
            log.error("Error handling room updated event", e);
            throw new RuntimeException("Failed to handle room updated event", e);
        }
    }

    private void handleRoomDeleted(DomainEvent event) {
        roomReadRepository.deleteById(event.getAggregateId());
        log.debug("Room deleted from ReadDB: {}", event.getAggregateId());
    }
}

