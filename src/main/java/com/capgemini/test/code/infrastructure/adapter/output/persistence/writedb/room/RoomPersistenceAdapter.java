package com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.room;

import com.capgemini.test.code.domain.room.model.Room;
import com.capgemini.test.code.domain.room.repository.RoomRepository;
import com.capgemini.test.code.infrastructure.adapter.output.event.EventPublisher;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * RoomPersistenceAdapter - Implementa RoomRepository (port)
 *
 * Responsabilidades:
 * 1. Persistir en WriteDB (PostgreSQL - previa)
 * 2. Publicar eventos a Kafka
 * 3. Mapear entre Room (dominio) y RoomEntity (JPA)
 *
 * Patrón: Adapter (infraestructura)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoomPersistenceAdapter implements RoomRepository {

    private final RoomJpaRepository jpaRepository;
    private final RoomMapper roomMapper;
    private final EventPublisher eventPublisher;

    /**
     * Guarda sala en WriteDB y publica evento a Kafka
     */
    @Override
    @Transactional(transactionManager = "writedbTransactionManager")
    public Room save(Room room) {
        log.debug("Saving room to WriteDB: {}", room.getId());

        RoomEntity entity = roomMapper.toEntity(room);
        RoomEntity saved = jpaRepository.save(entity);

        Room domainRoom = roomMapper.toDomain(saved);

        eventPublisher.publishRoomCreated(domainRoom);

        log.debug("Room saved and event published: {}", domainRoom.getId());
        return domainRoom;
    }

    /**
     * Busca sala en WriteDB por ID
     * Lectura: optimizada con readOnly
     */
    @Override
    @Transactional(readOnly = true, transactionManager = "writedbTransactionManager")
    public Room findById(Long id) {
        log.debug("Finding room by ID in WriteDB: {}", id);
        return jpaRepository.findById(id)
            .map(roomMapper::toDomain)
            .orElse(null);
    }
}


