package com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RoomReadJpaRepository - ReadDB (PostgreSQL)
 * Interfaz de acceso a datos para salas en la BD de lectura (réplica)
 */
@Repository
public interface RoomReadJpaRepository extends JpaRepository<RoomReadEntity, Long> {
}

