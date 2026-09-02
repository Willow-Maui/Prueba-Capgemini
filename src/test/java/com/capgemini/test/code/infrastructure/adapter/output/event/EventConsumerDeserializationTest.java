package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.room.model.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para verificar que la deserialización de Jackson funciona correctamente
 * para las clases User y Room que tienen campos final y no poseen constructores sin argumentos.
 */
@SpringBootTest
@DisplayName("EventConsumer - Deserialization Tests")
public class EventConsumerDeserializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("User Deserialization")
    class UserDeserializationTests {

        private static final String USER_JSON = """
            {
                "id": 3,
                "name": "Prueba",
                "email": "prueba@prueba.com",
                "phone": "698986689",
                "dni": "98765432M",
                "role": "ADMIN",
                "roomId": 1
            }
            """;

        private static final String USER_JSON_WITHOUT_ID = """
            {
                "name": "Juan",
                "email": "juan@example.com",
                "phone": "123456789",
                "dni": "12345678A",
                "role": "SUPERADMIN",
                "roomId": 2
            }
            """;

        @Test
        @DisplayName("Should deserialize User with ID from JSON")
        public void testDeserializeUserWithId() throws Exception {
            // Act
            User user = objectMapper.readValue(USER_JSON, User.class);

            // Assert
            assertNotNull(user);
            assertEquals(3L, user.getId());
            assertEquals("Prueba", user.getName());
            assertEquals("prueba@prueba.com", user.getEmail());
            assertEquals("698986689", user.getPhone());
            assertEquals("98765432M", user.getDni());
            assertEquals("ADMIN", user.getRole().name());
            assertEquals(1L, user.getRoomId());
        }

        @Test
        @DisplayName("Should deserialize User without ID from JSON")
        public void testDeserializeUserWithoutId() throws Exception {
            // Act
            User user = objectMapper.readValue(USER_JSON_WITHOUT_ID, User.class);

            // Assert
            assertNotNull(user);
            assertNull(user.getId());
            assertEquals("Juan", user.getName());
            assertEquals("juan@example.com", user.getEmail());
            assertEquals("123456789", user.getPhone());
            assertEquals("12345678A", user.getDni());
            assertEquals("SUPERADMIN", user.getRole().name());
            assertEquals(2L, user.getRoomId());
        }

        @Test
        @DisplayName("Should handle optional phone field")
        public void testDeserializeUserWithoutPhone() throws Exception {
            String userJsonWithoutPhone = """
                {
                    "id": 5,
                    "name": "Carlos",
                    "email": "carlos@example.com",
                    "phone": null,
                    "dni": "87654321Z",
                    "role": "ADMIN",
                    "roomId": 3
                }
                """;

            // Act
            User user = objectMapper.readValue(userJsonWithoutPhone, User.class);

            // Assert
            assertNotNull(user);
            assertEquals(5L, user.getId());
            assertNull(user.getPhone());
        }
    }

    @Nested
    @DisplayName("Room Deserialization")
    class RoomDeserializationTests {

        private static final String ROOM_JSON = """
            {
                "id": 1,
                "name": "Sala de Reuniones A"
            }
            """;

        @Test
        @DisplayName("Should deserialize Room from JSON")
        public void testDeserializeRoom() throws Exception {
            // Act
            Room room = objectMapper.readValue(ROOM_JSON, Room.class);

            // Assert
            assertNotNull(room);
            assertEquals(1L, room.getId());
            assertEquals("Sala de Reuniones A", room.getName());
        }

        @Test
        @DisplayName("Should fail when deserializing Room with invalid ID")
        public void testDeserializeRoomWithInvalidId() throws Exception {
            String invalidRoomJson = """
                {
                    "id": 0,
                    "name": "Invalid Room"
                }
                """;

            // Act & Assert
            assertThrows(Exception.class, () ->
                objectMapper.readValue(invalidRoomJson, Room.class)
            );
        }
    }
}

