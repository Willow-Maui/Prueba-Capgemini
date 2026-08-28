package com.capgemini.test.code.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests del DTO de Usuario.
 *
 * Validación de la transferencia de datos entre capas.
 */
@DisplayName("UserDTO - Data Transfer Object")
class UserDTOTest {

  @Nested
  @DisplayName("Constructor y Builder")
  class ConstructorAndBuilder {

    @Test
    @DisplayName("Debe crear DTO con builder")
    void shouldCreateDTOWithBuilder() {
      // Act
      UserDTO dto = UserDTO.builder()
          .id(1L)
          .name("pablo")
          .email("pablo@example.com")
          .phone("677998899")
          .dni("23454234W")
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(dto).isNotNull();
      assertThat(dto.getId()).isEqualTo(1L);
      assertThat(dto.getName()).isEqualTo("pablo");
      assertThat(dto.getEmail()).isEqualTo("pablo@example.com");
    }

    @Test
    @DisplayName("Debe crear DTO con constructor por defecto")
    void shouldCreateDTOWithDefaultConstructor() {
      // Act
      UserDTO dto = new UserDTO();
      dto.setId(1L);
      dto.setName("pablo");
      dto.setEmail("pablo@example.com");

      // Assert
      assertThat(dto.getId()).isEqualTo(1L);
      assertThat(dto.getName()).isEqualTo("pablo");
    }
  }

  @Nested
  @DisplayName("Getters y Setters")
  class GettersAndSetters {

    @Test
    @DisplayName("Debe obtener y establecer todos los campos")
    void shouldGetAndSetAllFields() {
      // Arrange
      UserDTO dto = new UserDTO();

      // Act
      dto.setId(1L);
      dto.setName("pablo");
      dto.setEmail("pablo@example.com");
      dto.setPhone("677998899");
      dto.setDni("23454234W");
      dto.setRole("admin");
      dto.setRoomId(1L);

      // Assert
      assertThat(dto.getId()).isEqualTo(1L);
      assertThat(dto.getName()).isEqualTo("pablo");
      assertThat(dto.getEmail()).isEqualTo("pablo@example.com");
      assertThat(dto.getPhone()).isEqualTo("677998899");
      assertThat(dto.getDni()).isEqualTo("23454234W");
      assertThat(dto.getRole()).isEqualTo("admin");
      assertThat(dto.getRoomId()).isEqualTo(1L);
    }
  }

  @Nested
  @DisplayName("Igualdad y Hash")
  class EqualityAndHash {

    @Test
    @DisplayName("DTOs con mismos valores deben ser iguales")
    void shouldBeEqualWithSameValues() {
      // Arrange
      UserDTO dto1 = UserDTO.builder()
          .id(1L)
          .name("pablo")
          .email("pablo@example.com")
          .role("admin")
          .build();

      UserDTO dto2 = UserDTO.builder()
          .id(1L)
          .name("pablo")
          .email("pablo@example.com")
          .role("admin")
          .build();

      // Assert
      assertThat(dto1).isEqualTo(dto2);
    }

    @Test
    @DisplayName("DTOs con valores diferentes no deben ser iguales")
    void shouldNotBeEqualWithDifferentValues() {
      // Arrange
      UserDTO dto1 = UserDTO.builder()
          .id(1L)
          .name("pablo")
          .build();

      UserDTO dto2 = UserDTO.builder()
          .id(2L)
          .name("juan")
          .build();

      // Assert
      assertThat(dto1).isNotEqualTo(dto2);
    }
  }

  @Nested
  @DisplayName("Campos Opcionales")
  class OptionalFields {

    @Test
    @DisplayName("Debe permitir phone null")
    void shouldAllowNullPhone() {
      // Act
      UserDTO dto = UserDTO.builder()
          .id(1L)
          .name("pablo")
          .email("pablo@example.com")
          .phone(null)
          .role("admin")
          .build();

      // Assert
      assertThat(dto.getPhone()).isNull();
    }

    @Test
    @DisplayName("Debe permitir roomId null")
    void shouldAllowNullRoomId() {
      // Act
      UserDTO dto = UserDTO.builder()
          .id(1L)
          .name("pablo")
          .email("pablo@example.com")
          .roomId(null)
          .build();

      // Assert
      assertThat(dto.getRoomId()).isNull();
    }
  }

  @Nested
  @DisplayName("Validación de Transferencia de Datos")
  class DataTransfer {

    @Test
    @DisplayName("Debe mantener integridad de datos al transportar")
    void shouldMaintainDataIntegrity() {
      // Arrange
      String expectedName = "pablo";
      String expectedEmail = "pablo@example.com";
      String expectedDni = "23454234W";
      String expectedRol = "admin";
      Long expectedId = 42L;

      // Act
      UserDTO dto = UserDTO.builder()
          .id(expectedId)
          .name(expectedName)
          .email(expectedEmail)
          .dni(expectedDni)
          .role(expectedRol)
          .build();

      // Assert
      assertThat(dto.getId()).isEqualTo(expectedId);
      assertThat(dto.getName()).isEqualTo(expectedName);
      assertThat(dto.getEmail()).isEqualTo(expectedEmail);
      assertThat(dto.getDni()).isEqualTo(expectedDni);
      assertThat(dto.getRole()).isEqualTo(expectedRol);
    }
  }
}

