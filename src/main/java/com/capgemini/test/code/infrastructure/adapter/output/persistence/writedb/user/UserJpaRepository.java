package com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * UserJpaRepository - WriteDB (MySQL)
 * Interfaz de acceso a datos para usuarios en la BD de escritura
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    List<UserEntity> findByRoomId(Long roomId);

    Optional<UserEntity> findByIdAndRoomId(Long id, Long roomId);
}

