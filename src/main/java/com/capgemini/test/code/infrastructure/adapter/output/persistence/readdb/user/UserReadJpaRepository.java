package com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * UserReadJpaRepository - ReadDB (PostgreSQL)
 * Interfaz de acceso a datos para usuarios en la BD de lectura (réplica)
 */
@Repository
public interface UserReadJpaRepository extends JpaRepository<UserReadEntity, Long> {

    Optional<UserReadEntity> findByEmail(String email);

    List<UserReadEntity> findByRoomId(Long roomId);

    Optional<UserReadEntity> findByIdAndRoomId(Long id, Long roomId);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);
}

