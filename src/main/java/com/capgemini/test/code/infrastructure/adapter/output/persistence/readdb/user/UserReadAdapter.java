package com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.user.repository.UserRepository;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserReadAdapter - Implementa UserRepository para lecturas desde ReadDB
 *
 * Responsabilidades:
 * 1. Leer desde ReadDB (MySQL - nueva) - réplica desnormalizada
 * 2. Mapear entre UserReadEntity (JPA) y User (dominio)
 *
 * Patrón: Adapter para CQRS (Query side)
 * Nota: Este adaptador es eventual consistent
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserReadAdapter{

    private final UserReadJpaRepository jpaRepository;
    private final UserMapper userMapper;

    /**
     * Busca usuario en ReadDB por ID
     * Eventual consistent: puede estar desactualizado algunos ms
     */
    @Transactional(readOnly = true, transactionManager = "readdbTransactionManager")
    public UserDTO findById(Long id) {
        log.debug("Finding user by ID in ReadDB: {}", id);
        return jpaRepository.findById(id)
            .map(userMapper::readToDTO)
            .orElse(null);
    }

    /**
     * Busca usuario en ReadDB por ID y Room
     */
    @Transactional(readOnly = true, transactionManager = "readdbTransactionManager")
    public UserDTO findByIdAndRoomId(Long id, Long roomId) {
        log.debug("Finding user by ID and room ID in ReadDB: {} / {}", id, roomId);
        return jpaRepository.findByIdAndRoomId(id, roomId)
            .map(userMapper::readToDTO)
            .orElse(null);
    }


    /**
     * Busca usuario en ReadDB por email
     * Lectura: optimizada con readOnly
     */
    @Transactional(readOnly = true, transactionManager = "readdbTransactionManager")
    public UserDTO findByEmail(String email) {
        log.debug("Finding user by email in WriteDB: {}", email);
        return jpaRepository.findByEmail(email)
                .map(userMapper::readToDTO)
                .orElse(null);
    }

    /**
     * Verifica si email ya existe (duplicado)
     * Lectura: optimizada con readOnly
     */
    @Transactional(readOnly = true, transactionManager = "readdbTransactionManager")
    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists in WriteDB: {}", email);
        return jpaRepository.existsByEmail(email);
    }

    /**
     * Verifica si DNI ya existe (duplicado)
     * Lectura: optimizada con readOnly
     */
    @Transactional(readOnly = true, transactionManager = "readdbTransactionManager")
    public boolean existsByDni(String dni) {
        log.debug("Checking if DNI exists in WriteDB: {}", dni);
        return jpaRepository.existsByDni(dni);
    }

}


