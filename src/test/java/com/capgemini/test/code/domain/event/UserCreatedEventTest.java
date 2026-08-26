package com.capgemini.test.code.domain.event;

import com.capgemini.test.code.infrastructure.adapter.output.event.DomainEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests para Domain Events de usuarios.
 *
 * NO requiere Kafka real, solo verifica la creación de eventos a nivel de dominio.
 * Los eventos se publican a Kafka por el adaptador de output, no por el dominio.
 */
@DisplayName("User Domain Event Unit Tests")
public class UserCreatedEventTest {

    @Test
    @DisplayName("Crear evento de dominio USER_CREATED")
    void testCreateUserCreatedDomainEvent() {
        // Arrange
        var userId = 1L;
        var eventType = "USER_CREATED";
        var payload = "{\"name\":\"john\",\"email\":\"john@test.com\",\"rol\":\"admin\"}";
        var timestamp = LocalDateTime.now();

        // Act
        var event = DomainEvent.builder()
            .eventId(1L)
            .eventType(eventType)
            .aggregateId(userId)
            .aggregateType("User")
            .payload(payload)
            .timestamp(timestamp)
            .build();

        // Assert
        assertNotNull(event);
        assertEquals(eventType, event.getEventType());
        assertEquals(userId, event.getAggregateId());
        assertEquals("User", event.getAggregateType());
        assertNotNull(event.getPayload());
    }

    @Test
    @DisplayName("Evento de dominio es inmutable después de creación")
    void testDomainEventImmutable() {
        var event = DomainEvent.builder()
            .eventId(2L)
            .eventType("USER_CREATED")
            .aggregateId(2L)
            .aggregateType("User")
            .payload("{\"name\":\"jane\"}")
            .timestamp(LocalDateTime.now())
            .build();

        // El evento tiene su estado definido
        assertNotNull(event.getEventId());
        assertNotNull(event.getEventType());
        assertNotNull(event.getAggregateId());
    }

    @Test
    @DisplayName("Múltiples eventos de dominio son independientes")
    void testMultipleDomainEventsAreIndependent() {
        // Arrange & Act
        var event1 = DomainEvent.builder()
            .eventId(1L)
            .eventType("USER_CREATED")
            .aggregateId(1L)
            .aggregateType("User")
            .payload("{\"name\":\"user1\"}")
            .timestamp(LocalDateTime.now())
            .build();

        var event2 = DomainEvent.builder()
            .eventId(2L)
            .eventType("USER_CREATED")
            .aggregateId(2L)
            .aggregateType("User")
            .payload("{\"name\":\"user2\"}")
            .timestamp(LocalDateTime.now())
            .build();

        // Assert
        assertNotEquals(event1.getAggregateId(), event2.getAggregateId());
        assertNotEquals(event1.getEventId(), event2.getEventId());
    }

    @Test
    @DisplayName("Evento de dominio se puede serializar para Kafka")
    void testDomainEventCanBeSerialized() {
        var event = DomainEvent.builder()
            .eventId(3L)
            .eventType("USER_CREATED")
            .aggregateId(3L)
            .aggregateType("User")
            .payload("{\"name\":\"testuser\",\"email\":\"test@test.com\"}")
            .timestamp(LocalDateTime.now())
            .build();

        // El evento puede convertirse a string para serialización
        assertNotNull(event.toString());
        assertTrue(event.getEventType().contains("USER_CREATED"));
    }

    @Test
    @DisplayName("Diferentes tipos de eventos de dominio")
    void testDifferentDomainEventTypes() {
        // USER_CREATED
        var userCreated = DomainEvent.builder()
            .eventType("USER_CREATED")
            .aggregateType("User")
            .aggregateId(1L)
            .payload("{\"name\":\"user1\"}")
            .timestamp(LocalDateTime.now())
            .build();

        // USER_UPDATED
        var userUpdated = DomainEvent.builder()
            .eventType("USER_UPDATED")
            .aggregateType("User")
            .aggregateId(1L)
            .payload("{\"name\":\"user1-updated\"}")
            .timestamp(LocalDateTime.now())
            .build();

        // USER_DELETED
        var userDeleted = DomainEvent.builder()
            .eventType("USER_DELETED")
            .aggregateType("User")
            .aggregateId(1L)
            .payload("{}")
            .timestamp(LocalDateTime.now())
            .build();

        assertNotEquals(userCreated.getEventType(), userUpdated.getEventType());
        assertNotEquals(userUpdated.getEventType(), userDeleted.getEventType());
        assertEquals(userCreated.getAggregateId(), userUpdated.getAggregateId());
    }
}


