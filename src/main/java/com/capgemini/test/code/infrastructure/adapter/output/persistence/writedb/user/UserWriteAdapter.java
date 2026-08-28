package com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.user;

import com.capgemini.test.code.application.dto.UserDTO;
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
public class UserWriteAdapter {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;
    private final EventPublisher eventPublisher;

    /**
     * Guarda usuario en WriteDB y publica evento a Kafka
     * Transacción: garantiza que se guarda O se publica, no solo uno
     */
    @Transactional(transactionManager = "writedbTransactionManager")
    public UserDTO save(UserDTO user) {
        log.debug("Saving user to WriteDB: {}", user.getEmail());

        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);

        UserDTO userDTO = userMapper.writeToDomain(saved);

        // Publicar evento a Kafka (dentro de la transacción)
        eventPublisher.publishUserCreated(userDTO);

        log.debug("User saved and event published: {}", userDTO.getId());
        return userDTO;
    }

}


