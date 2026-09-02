package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user.UserReadEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user.UserReadJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

/**
 * UserEventSyncService - Sincroniza eventos de usuario a ReadDB (MySQL)
 *
 * Responsabilidades:
 * 1. Deserializar eventos de usuario desde Kafka
 * 2. Persistir cambios en ReadDB con transacciones
 * 3. Garantizar consistencia eventual
 *
 * Patrón: Service con @Transactional explícito para ReadDB
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventSyncService {

    private final UserReadJpaRepository userReadRepository;
    private final ObjectMapper objectMapper;

    @PersistenceContext(unitName = "readdbEntityManagerFactory")
    private EntityManager entityManager;

    /**
     * Maneja evento USER_CREATED - Crea usuario en ReadDB
     * Ejecutado dentro de transacción ReadDB
     */
    @Transactional(transactionManager = "readdbTransactionManager",propagation = Propagation.REQUIRES_NEW)
    public void handleUserCreated(DomainEvent event) {
        try {
            log.debug("Processing USER_CREATED event with payload: {}", event.getPayload());

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

            log.info("✓ User synchronized to ReadDB: {}", user.getId());
        } catch (Exception e) {
            log.error("✗ Error handling user created event", e);
            throw new RuntimeException("Failed to handle user created event", e);
        }
    }

    /**
     * Maneja evento USER_UPDATED - Actualiza usuario en ReadDB
     * Ejecutado dentro de transacción ReadDB
     */
    @Transactional(transactionManager = "readdbTransactionManager")
    public void handleUserUpdated(DomainEvent event) {
        try {
            log.debug("Processing USER_UPDATED event with payload: {}", event.getPayload());

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
                    log.info("✓ User updated in ReadDB: {}", user.getId());
                },
                () -> {
                    log.warn("⚠ User not found in ReadDB for update: {}. Creating instead.", user.getId());
                    handleUserCreated(event);
                }
            );
        } catch (Exception e) {
            log.error("✗ Error handling user updated event", e);
            throw new RuntimeException("Failed to handle user updated event", e);
        }
    }

    /**
     * Maneja evento USER_DELETED - Elimina usuario de ReadDB
     * Ejecutado dentro de transacción ReadDB
     */
    @Transactional(transactionManager = "readdbTransactionManager")
    public void handleUserDeleted(DomainEvent event) {
        try {
            log.debug("Processing USER_DELETED event for user ID: {}", event.getAggregateId());

            userReadRepository.deleteById(event.getAggregateId());
            log.info("✓ User deleted from ReadDB: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("✗ Error handling user deleted event", e);
            throw new RuntimeException("Failed to handle user deleted event", e);
        }
    }
}

