package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.capgemini.test.code.domain.room.model.Room;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.room.RoomReadEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.room.RoomReadJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * RoomEventSyncService - Sincroniza eventos de sala a ReadDB (MySQL)
 *
 * Responsabilidades:
 * 1. Deserializar eventos de sala desde Kafka
 * 2. Persistir cambios en ReadDB con transacciones
 * 3. Garantizar consistencia eventual
 *
 * Patrón: Service con @Transactional explícito para ReadDB
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoomEventSyncService {

    private final RoomReadJpaRepository roomReadRepository;
    private final ObjectMapper objectMapper;

    /**
     * Maneja evento ROOM_CREATED - Crea sala en ReadDB
     * Ejecutado dentro de transacción ReadDB
     */
    @Transactional(transactionManager = "readdbTransactionManager")
    public void handleRoomCreated(DomainEvent event) {
        try {
            log.debug("Processing ROOM_CREATED event with payload: {}", event.getPayload());

            Room room = objectMapper.readValue(event.getPayload(), Room.class);

            RoomReadEntity readEntity = RoomReadEntity.builder()
                .id(room.getId())
                .name(room.getName())
                .createdAt(event.getTimestamp())
                .lastSyncAt(LocalDateTime.now())
                .build();

            roomReadRepository.save(readEntity);
            log.info("✓ Room synchronized to ReadDB: {}", room.getId());
        } catch (Exception e) {
            log.error("✗ Error handling room created event", e);
            throw new RuntimeException("Failed to handle room created event", e);
        }
    }

    /**
     * Maneja evento ROOM_UPDATED - Actualiza sala en ReadDB
     * Ejecutado dentro de transacción ReadDB
     */
    @Transactional(transactionManager = "readdbTransactionManager")
    public void handleRoomUpdated(DomainEvent event) {
        try {
            log.debug("Processing ROOM_UPDATED event with payload: {}", event.getPayload());

            Room room = objectMapper.readValue(event.getPayload(), Room.class);

            roomReadRepository.findById(event.getAggregateId()).ifPresentOrElse(
                existing -> {
                    existing.setName(room.getName());
                    roomReadRepository.save(existing);
                    log.info("✓ Room updated in ReadDB: {}", room.getId());
                },
                () -> {
                    log.warn("⚠ Room not found in ReadDB for update: {}. Creating instead.", room.getId());
                    handleRoomCreated(event);
                }
            );
        } catch (Exception e) {
            log.error("✗ Error handling room updated event", e);
            throw new RuntimeException("Failed to handle room updated event", e);
        }
    }

    /**
     * Maneja evento ROOM_DELETED - Elimina sala de ReadDB
     * Ejecutado dentro de transacción ReadDB
     */
    @Transactional(transactionManager = "readdbTransactionManager")
    public void handleRoomDeleted(DomainEvent event) {
        try {
            log.debug("Processing ROOM_DELETED event for room ID: {}", event.getAggregateId());

            roomReadRepository.deleteById(event.getAggregateId());
            log.info("✓ Room deleted from ReadDB: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("✗ Error handling room deleted event", e);
            throw new RuntimeException("Failed to handle room deleted event", e);
        }
    }
}

