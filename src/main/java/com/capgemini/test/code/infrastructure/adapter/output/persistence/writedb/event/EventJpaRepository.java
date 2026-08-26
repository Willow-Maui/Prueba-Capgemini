package com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * EventJpaRepository - Event Store (WriteDB - MySQL)
 * Repositorio para gestionar eventos no publicados y auditoría
 */
@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findByPublishedFalseOrderByCreatedAtAsc();

    List<EventEntity> findByAggregateTypeAndAggregateIdOrderByCreatedAtAsc(
        String aggregateType, Long aggregateId);
}

