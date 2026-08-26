package com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.user;

import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.user.repository.UserRepository;
import com.capgemini.test.code.infrastructure.adapter.output.event.EventPublisher;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserPersistenceAdapter - Implementa UserRepository (port)
 *
 * Responsabilidades:
 * 1. Persistir en WriteDB (PostgreSQL - previa)
 * 2. Publicar eventos a Kafka
 * 3. Mapear entre User (dominio) y UserEntity (JPA)
 *
 * Patrón: Adapter (infraestructura)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserPersistenceAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;
    private final EventPublisher eventPublisher;

    /**
     * Guarda usuario en WriteDB y publica evento a Kafka
     * Transacción: garantiza que se guarda O se publica, no solo uno
     */
    @Override
    @Transactional(transactionManager = "writedbTransactionManager")
    public User save(User user) {
        log.debug("Saving user to WriteDB: {}", user.getEmail());

        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);

        User domainUser = userMapper.toDomain(saved);

        // Publicar evento a Kafka (dentro de la transacción)
        eventPublisher.publishUserCreated(domainUser);

        log.debug("User saved and event published: {}", domainUser.getId());
        return domainUser;
    }

    /**
     * Busca usuario en WriteDB por ID
     * Lectura: optimizada con readOnly
     */
    @Override
    @Transactional(readOnly = true, transactionManager = "writedbTransactionManager")
    public User findById(Long id) {
        log.debug("Finding user by ID in WriteDB: {}", id);
        return jpaRepository.findById(id)
            .map(userMapper::toDomain)
            .orElse(null);
    }

    /**
     * Busca usuario en WriteDB por email
     * Lectura: optimizada con readOnly
     */
    @Override
    @Transactional(readOnly = true, transactionManager = "writedbTransactionManager")
    public User findByEmail(String email) {
        log.debug("Finding user by email in WriteDB: {}", email);
        return jpaRepository.findByEmail(email)
            .map(userMapper::toDomain)
            .orElse(null);
    }

    /**
     * Verifica si email ya existe (duplicado)
     * Lectura: optimizada con readOnly
     */
    @Override
    @Transactional(readOnly = true, transactionManager = "writedbTransactionManager")
    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists in WriteDB: {}", email);
        return jpaRepository.existsByEmail(email);
    }

    /**
     * Verifica si DNI ya existe (duplicado)
     * Lectura: optimizada con readOnly
     */
    @Override
    @Transactional(readOnly = true, transactionManager = "writedbTransactionManager")
    public boolean existsByDni(String dni) {
        log.debug("Checking if DNI exists in WriteDB: {}", dni);
        return jpaRepository.existsByDni(dni);
    }

    /**
     * Busca usuario en WriteDB por ID y Room
     * Lectura: optimizada con readOnly
     */
    @Override
    @Transactional(readOnly = true, transactionManager = "writedbTransactionManager")
    public User findByIdAndRoomId(Long id, Long roomId) {
        log.debug("Finding user by ID and room ID in WriteDB: {} / {}", id, roomId);
        return jpaRepository.findByIdAndRoomId(id, roomId)
            .map(userMapper::toDomain)
            .orElse(null);
    }
}


