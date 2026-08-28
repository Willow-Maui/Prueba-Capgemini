package com.capgemini.test.code.application.mapper;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios para UserMapper.
 *
 * Validación del mapeo entre User (dominio) y UserDTO (aplicación).
 */
@DisplayName("UserMapper - Mapeo User ↔ UserDTO")
class UserMapperTest {

  @Nested
  @DisplayName("Mapear User a UserDTO")
  class MapUserToDto {

    @Test
    @DisplayName("Debe mapear usuario ADMIN completo a DTO")
    void shouldMapAdminUserToDto() {
      // Arrange
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(42L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(42L);
      assertThat(result.getName()).isEqualTo("pablo");
      assertThat(result.getEmail()).isEqualTo("pablo@example.com");
      assertThat(result.getDni()).isEqualTo("23454234W");
      assertThat(result.getPhone()).isNull();
      assertThat(result.getRole()).isEqualTo("ADMIN");
      assertThat(result.getRoomId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Debe mapear usuario SUPERADMIN con teléfono a DTO")
    void shouldMapSuperadminUserWithPhoneToDto() {
      // Arrange
      User user = User.builder()
          .name("juan")
          .email("juan@example.com")
          .dni("12345678A")
          .phone("677998899")
          .role("superadmin")
          .roomId(1L)
          .buildWithId(43L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(43L);
      assertThat(result.getName()).isEqualTo("juan");
      assertThat(result.getEmail()).isEqualTo("juan@example.com");
      assertThat(result.getDni()).isEqualTo("12345678A");
      assertThat(result.getPhone()).isEqualTo("677998899");
      assertThat(result.getRole()).isEqualTo("SUPERADMIN");
      assertThat(result.getRoomId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Debe retornar null cuando User es null")
    void shouldReturnNullWhenUserIsNull() {
      // Act
      UserDTO result = UserMapper.toDTO((User)null);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("Debe mapear todos los campos correctamente")
    void shouldMapAllFieldsCorrectly() {
      // Arrange
      User user = User.builder()
          .name("maria")
          .email("maria@test.com")
          .dni("99887766Z")
          .phone("612345678")
          .role("admin")
          .roomId(5L)
          .buildWithId(100L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result)
          .satisfies(dto -> {
            assertThat(dto.getId()).isEqualTo(100L);
            assertThat(dto.getName()).isEqualTo("maria");
            assertThat(dto.getEmail()).isEqualTo("maria@test.com");
            assertThat(dto.getDni()).isEqualTo("99887766Z");
            assertThat(dto.getPhone()).isEqualTo("612345678");
            assertThat(dto.getRole()).isEqualTo("ADMIN");
            assertThat(dto.getRoomId()).isEqualTo(5L);
          });
    }

    @Test
    @DisplayName("Debe preservar el ID del usuario")
    void shouldPreserveUserId() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("test@example.com")
          .dni("11111111A")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(999L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getId()).isEqualTo(999L);
    }

    @Test
    @DisplayName("Debe preservar el nombre del usuario")
    void shouldPreserveName() {
      // Arrange
      User user = User.builder()
          .name("testus")
          .email("test@example.com")
          .dni("22222222B")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(1L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getName()).isEqualTo("testus");
    }

    @Test
    @DisplayName("Debe preservar el email del usuario")
    void shouldPreserveEmail() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("unique_email@example.com")
          .dni("33333333C")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(1L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getEmail()).isEqualTo("unique_email@example.com");
    }

    @Test
    @DisplayName("Debe preservar el DNI del usuario")
    void shouldPreserveDni() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("test@example.com")
          .dni("unique_dni_123")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(1L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getDni()).isEqualTo("unique_dni_123");
    }

    @Test
    @DisplayName("Debe preservar el teléfono cuando no es null")
    void shouldPreservePhoneWhenNotNull() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("test@example.com")
          .dni("44444444D")
          .phone("999888777")
          .role("admin")
          .roomId(1L)
          .buildWithId(1L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getPhone()).isEqualTo("999888777");
    }

    @Test
    @DisplayName("Debe preservar null cuando teléfono es null")
    void shouldPreserveNullPhoneWhenNull() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("test@example.com")
          .dni("55555555E")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(1L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getPhone()).isNull();
    }

    @Test
    @DisplayName("Debe preservar el rol del usuario (ADMIN)")
    void shouldPreserveAdminRole() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("test@example.com")
          .dni("66666666F")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(1L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Debe preservar el rol del usuario (SUPERADMIN)")
    void shouldPreserveSuperadminRole() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("test@example.com")
          .dni("77777777G")
          .phone("123456789")
          .role("superadmin")
          .roomId(1L)
          .buildWithId(1L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getRole()).isEqualTo("SUPERADMIN");
    }

    @Test
    @DisplayName("Debe preservar el ID de la sala del usuario")
    void shouldPreserveRoomId() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("test@example.com")
          .dni("88888888H")
          .phone(null)
          .role("admin")
          .roomId(999L)
          .buildWithId(1L);

      // Act
      UserDTO result = UserMapper.toDTO(user);

      // Assert
      assertThat(result.getRoomId()).isEqualTo(999L);
    }
  }

  @Nested
  @DisplayName("Consistencia del mapeo")
  class ConsistencyMapping {

    @Test
    @DisplayName("Debe producir mapeos consistentes para el mismo usuario")
    void shouldProduceConsistentMappings() {
      // Arrange
      User user = User.builder()
          .name("test")
          .email("test@example.com")
          .dni("11111111A")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(42L);

      // Act
      UserDTO result1 = UserMapper.toDTO(user);
      UserDTO result2 = UserMapper.toDTO(user);

      // Assert
      assertThat(result1).isEqualTo(result2);
    }

    @Test
    @DisplayName("Debe mapear usuarios diferentes de forma independiente")
    void shouldMapDifferentUsersIndependently() {
      // Arrange
      User user1 = User.builder()
          .name("user1")
          .email("user1@example.com")
          .dni("11111111A")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(1L);

      User user2 = User.builder()
          .name("user2")
          .email("user2@example.com")
          .dni("22222222B")
          .phone("111111111")
          .role("superadmin")
          .roomId(2L)
          .buildWithId(2L);

      // Act
      UserDTO result1 = UserMapper.toDTO(user1);
      UserDTO result2 = UserMapper.toDTO(user2);

      // Assert
      assertThat(result1.getId()).isEqualTo(1L);
      assertThat(result1.getName()).isEqualTo("user1");
      assertThat(result2.getId()).isEqualTo(2L);
      assertThat(result2.getName()).isEqualTo("user2");
      assertThat(result1).isNotEqualTo(result2);
    }
  }
}


