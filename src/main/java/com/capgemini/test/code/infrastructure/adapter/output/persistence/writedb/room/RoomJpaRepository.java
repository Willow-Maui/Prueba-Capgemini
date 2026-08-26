package com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RoomJpaRepository - WriteDB (MySQL)
 * Interfaz de acceso a datos para salas en la BD de escritura
 */
@Repository
public interface RoomJpaRepository extends JpaRepository<RoomEntity, Long> {
}

