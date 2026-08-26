package com.capgemini.test.code.domain.room.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests unitarios para la entidad de dominio Room.
 *
 * Una sala contiene N usuarios. Cada usuario pertenece a una única sala.
 * La sala es una entidad de dominio que debe validar su ID y nombre.
 */
@DisplayName("Room Entity - Validaciones de Dominio")
class RoomTest {

  @Nested
  @DisplayName("Crear sala válida")
  class CreateValidRoom {

    @Test
    @DisplayName("Debe crear sala con ID y nombre válidos")
    void shouldCreateValidRoom() {
      // Arrange & Act
      Room room = new Room(1L, "Sala 1");

      // Assert
      assertThat(room).isNotNull();
      assertThat(room.getId()).isEqualTo(1L);
      assertThat(room.getName()).isEqualTo("Sala 1");
    }

    @Test
    @DisplayName("Debe crear sala con ID grande (validar que soporta números altos)")
    void shouldCreateRoomWithLargeId() {
      // Arrange & Act
      Room room = new Room(999999999L, "Sala Grande");

      // Assert
      assertThat(room.getId()).isEqualTo(999999999L);
    }

    @Test
    @DisplayName("Debe crear sala con nombre largo")
    void shouldCreateRoomWithLongName() {
      // Arrange & Act
      String longName = "Sala de reuniones con nombre muy largo para verificar que no hay límite";
      Room room = new Room(1L, longName);

      // Assert
      assertThat(room.getName()).isEqualTo(longName);
    }

    @Test
    @DisplayName("Debe crear sala con nombre especial con caracteres")
    void shouldCreateRoomWithSpecialCharacters() {
      // Arrange & Act
      Room room = new Room(1L, "Sala #1 (Reuniones) - Principal");

      // Assert
      assertThat(room.getName()).isEqualTo("Sala #1 (Reuniones) - Principal");
    }
  }

  @Nested
  @DisplayName("Validar ID de sala")
  class ValidateRoomId {

    @Test
    @DisplayName("Debe rechazar ID igual a cero")
    void shouldRejectZeroId() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> new Room(0L, "Sala 1"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("positive");
    }

    @Test
    @DisplayName("Debe rechazar ID negativo")
    void shouldRejectNegativeId() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> new Room(-1L, "Sala 1"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("positive");
    }

    @Test
    @DisplayName("Debe rechazar ID nulo")
    void shouldRejectNullId() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> new Room(null, "Sala 1"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("positive");
    }

    @Test
    @DisplayName("Debe aceptar ID 1 (mínimo permitido)")
    void shouldAcceptIdOne() {
      // Arrange & Act
      Room room = new Room(1L, "Sala 1");

      // Assert
      assertThat(room.getId()).isEqualTo(1L);
    }
  }

  @Nested
  @DisplayName("Validar nombre de sala")
  class ValidateRoomName {

    @Test
    @DisplayName("Debe rechazar nombre nulo")
    void shouldRejectNullName() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> new Room(1L, null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Debe aceptar nombre vacío (solo validación de nulo)")
    void shouldAcceptEmptyName() {
      // Arrange & Act
      Room room = new Room(1L, "");

      // Assert
      assertThat(room.getName()).isEmpty();
    }

    @Test
    @DisplayName("Debe aceptar nombre con espacios")
    void shouldAcceptNameWithSpaces() {
      // Arrange & Act
      Room room = new Room(1L, "  Sala con espacios  ");

      // Assert
      // No se trimean espacios, se guardan tal cual
      assertThat(room.getName()).isEqualTo("  Sala con espacios  ");
    }
  }

  @Nested
  @DisplayName("Igualdad de salas (equals)")
  class RoomEquality {

    @Test
    @DisplayName("Dos salas con mismo ID deben ser iguales")
    void shouldBeEqualWhenSameId() {
      // Arrange
      Room room1 = new Room(1L, "Sala 1");
      Room room2 = new Room(1L, "Sala 1");

      // Act & Assert
      assertThat(room1).isEqualTo(room2);
    }

    @Test
    @DisplayName("Dos salas con mismo ID pero diferente nombre deben ser iguales (ID es el identificador)")
    void shouldBeEqualWhenSameIdDifferentName() {
      // Arrange
      Room room1 = new Room(1L, "Sala 1");
      Room room2 = new Room(1L, "Sala A");

      // Act & Assert
      // En DDD, la identidad se define por el ID, no por el nombre
      assertThat(room1).isEqualTo(room2);
    }

    @Test
    @DisplayName("Dos salas con diferente ID no deben ser iguales")
    void shouldNotBeEqualWhenDifferentId() {
      // Arrange
      Room room1 = new Room(1L, "Sala 1");
      Room room2 = new Room(2L, "Sala 1");

      // Act & Assert
      assertThat(room1).isNotEqualTo(room2);
    }

    @Test
    @DisplayName("Una sala debe ser igual a sí misma")
    void shouldBeEqualToItself() {
      // Arrange
      Room room = new Room(1L, "Sala 1");

      // Act & Assert
      assertThat(room).isEqualTo(room);
    }

    @Test
    @DisplayName("Una sala no debe ser igual a null")
    void shouldNotBeEqualToNull() {
      // Arrange
      Room room = new Room(1L, "Sala 1");

      // Act & Assert
      assertThat(room).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Una sala no debe ser igual a un objeto de otra clase")
    void shouldNotBeEqualToOtherType() {
      // Arrange
      Room room = new Room(1L, "Sala 1");
      String other = "Sala 1";

      // Act & Assert
      assertThat(room).isNotEqualTo(other);
    }
  }

  @Nested
  @DisplayName("HashCode de salas")
  class RoomHashCode {

    @Test
    @DisplayName("Dos salas iguales deben tener el mismo hashCode")
    void shouldHaveSameHashCodeWhenEqual() {
      // Arrange
      Room room1 = new Room(1L, "Sala 1");
      Room room2 = new Room(1L, "Sala 2");

      // Act
      int hash1 = room1.hashCode();
      int hash2 = room2.hashCode();

      // Assert
      assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    @DisplayName("Dos salas diferentes deben poder tener diferente hashCode")
    void shouldDifferentHashCodeWhenNotEqual() {
      // Arrange
      Room room1 = new Room(1L, "Sala 1");
      Room room2 = new Room(2L, "Sala 1");

      // Act
      int hash1 = room1.hashCode();
      int hash2 = room2.hashCode();

      // Assert
      // No garantiza que sean diferentes, pero es probable
      assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    @DisplayName("HashCode debe ser consistente para la misma sala")
    void shouldHaveConsistentHashCode() {
      // Arrange
      Room room = new Room(1L, "Sala 1");

      // Act
      int hash1 = room.hashCode();
      int hash2 = room.hashCode();

      // Assert
      assertThat(hash1).isEqualTo(hash2);
    }
  }

  @Nested
  @DisplayName("Representación en String (toString)")
  class RoomToString {

    @Test
    @DisplayName("Debe contener ID en toString()")
    void shouldContainIdInToString() {
      // Arrange
      Room room = new Room(1L, "Sala 1");

      // Act
      String representation = room.toString();

      // Assert
      assertThat(representation).contains("1");
    }

    @Test
    @DisplayName("Debe contener nombre en toString()")
    void shouldContainNameInToString() {
      // Arrange
      Room room = new Room(1L, "Sala 1");

      // Act
      String representation = room.toString();

      // Assert
      assertThat(representation).contains("Sala 1");
    }

    @Test
    @DisplayName("toString() debe devolver un String no nulo")
    void shouldReturnNonNullString() {
      // Arrange
      Room room = new Room(1L, "Sala 1");

      // Act & Assert
      assertThat(room.toString()).isNotNull();
    }
  }

  @Nested
  @DisplayName("Getters")
  class Getters {

    @Test
    @DisplayName("getId() debe retornar el ID establecido")
    void shouldReturnId() {
      // Arrange
      Room room = new Room(42L, "Sala 42");

      // Act
      Long id = room.getId();

      // Assert
      assertThat(id).isEqualTo(42L);
    }

    @Test
    @DisplayName("getName() debe retornar el nombre establecido")
    void shouldReturnName() {
      // Arrange
      Room room = new Room(1L, "Mi Sala");

      // Act
      String name = room.getName();

      // Assert
      assertThat(name).isEqualTo("Mi Sala");
    }
  }
}

