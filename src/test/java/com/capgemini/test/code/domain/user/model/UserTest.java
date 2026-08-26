package com.capgemini.test.code.domain.user.model;

import com.capgemini.test.code.domain.user.exceptions.InvalidDniException;
import com.capgemini.test.code.domain.user.exceptions.InvalidEmailException;
import com.capgemini.test.code.domain.user.exceptions.InvalidPhoneException;
import com.capgemini.test.code.domain.user.exceptions.InvalidRoleException;
import com.capgemini.test.code.domain.user.exceptions.InvalidUserNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests unitarios para la entidad de dominio User.
 *
 * Requisitos del README:
 * - Nombre: máximo 6 caracteres
 * - Email: debe contener @ y .
 * - Email: debe ser único (validado en layer superior)
 * - Rol: solo "admin" o "superadmin"
 * - DNI: no vacío (validación contra API externa en application layer)
 * - Phone: obligatorio para SUPERADMIN, opcional para ADMIN
 */
@DisplayName("User Entity - Validaciones de Dominio")
class UserTest {

  @Nested
  @DisplayName("Crear usuario válido")
  class CreateValidUser {

    @Test
    @DisplayName("Debe crear usuario ADMIN con datos válidos")
    void shouldCreateValidAdminUser() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")                    // ✓ 5 caracteres <= 6
          .email("pablo@example.com")      // ✓ contiene @ y .
          .dni("23454234W")                // ✓ no vacío
          .phone(null)                     // ✓ opcional para ADMIN
          .role("admin")                   // ✓ válido
          .roomId(1L)                      // ✓ positivo
          .build();

      // Assert
      assertThat(user).isNotNull();
      assertThat(user.getName()).isEqualTo("pablo");
      assertThat(user.getEmail()).isEqualTo("pablo@example.com");
      assertThat(user.getDni()).isEqualTo("23454234W");
      assertThat(user.getPhone()).isNull();
      assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
      assertThat(user.getRoomId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Debe crear usuario SUPERADMIN con datos válidos")
    void shouldCreateValidSuperadminUser() {
      // Arrange & Act
      User user = User.builder()
          .name("juan")                     // ✓ 4 caracteres <= 6
          .email("juan@test.com")          // ✓ contiene @ y .
          .dni("12345678A")                // ✓ no vacío
          .phone("677998899")              // ✓ obligatorio para SUPERADMIN
          .role("superadmin")              // ✓ válido
          .roomId(2L)                      // ✓ positivo
          .build();

      // Assert
      assertThat(user).isNotNull();
      assertThat(user.getName()).isEqualTo("juan");
      assertThat(user.getPhone()).isEqualTo("677998899");
      assertThat(user.getRole()).isEqualTo(UserRole.SUPERADMIN);
    }

    @Test
    @DisplayName("Debe crear usuario con nombre de 6 caracteres (máximo permitido)")
    void shouldCreateUserWithMaxName6Chars() {
      // Arrange & Act
      User user = User.builder()
          .name("pablon")                  // ✓ exactamente 6 caracteres
          .email("pablon@test.com")
          .dni("12345678A")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getName()).hasSize(6).isEqualTo("pablon");
    }

    @Test
    @DisplayName("Debe crear usuario y trimear espacios en blanco (input ya validado)")
    void shouldTrimWhitespaces() {
      // Arrange & Act
      // Nota: La validación ocurre ANTES del trimeo, así que el input debe cumplir límites ANTES de trimear
      // "pablo" (5 chars) es válido sin espacios
      User user = User.builder()
          .name("pablo")                   // Ya valida <= 6 caracteres sin espacios extras
          .email("  pablo@example.com  ")  // espacios a los lados en email
          .dni("  23454234W  ")            // espacios a los lados en DNI
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getName()).isEqualTo("pablo");
      assertThat(user.getEmail()).isEqualTo("pablo@example.com");
      assertThat(user.getDni()).isEqualTo("23454234W");
    }

    @Test
    @DisplayName("Debe crear usuario con buildWithId() para operaciones de lectura")
    void shouldCreateUserWithIdUsingBuildWithId() {
      // Arrange
      Long userId = 42L;

      // Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(userId);

      // Assert
      assertThat(user.getId()).isEqualTo(42L);
      assertThat(user.getName()).isEqualTo("pablo");
    }
  }

  @Nested
  @DisplayName("Validar nombre de usuario")
  class ValidateUserName {

    @Test
    @DisplayName("Debe rechazar nombre vacío")
    void shouldRejectEmptyName() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidUserNameException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("Debe rechazar nombre nulo")
    void shouldRejectNullName() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name(null)
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidUserNameException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("Debe rechazar nombre con solo espacios en blanco")
    void shouldRejectNameWithOnlyWhitespaces() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("   ")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidUserNameException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("Debe rechazar nombre que excede 6 caracteres (Requisito del README)")
    void shouldRejectNameExceeding6Chars() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablogarcia")             // 11 caracteres - VIOLACIÓN DE REQUISITO
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidUserNameException.class)
          .hasMessageContaining("exceed 6 characters");
    }

    @Test
    @DisplayName("Debe rechazar nombre con 7 caracteres")
    void shouldRejectNameWith7Chars() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablone")                 // 7 caracteres
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidUserNameException.class)
          .hasMessageContaining("exceed 6 characters");
    }
  }

  @Nested
  @DisplayName("Validar email de usuario")
  class ValidateEmail {

    @Test
    @DisplayName("Debe rechazar email vacío")
    void shouldRejectEmptyEmail() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidEmailException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("Debe rechazar email nulo")
    void shouldRejectNullEmail() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email(null)
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidEmailException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("Debe rechazar email sin @ (Requisito del README)")
    void shouldRejectEmailWithoutAtSymbol() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pabloemail.com")         // FALTA @ - VIOLACIÓN DE REQUISITO
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidEmailException.class)
          .hasMessageContaining("@");
    }

    @Test
    @DisplayName("Debe rechazar email sin . (Requisito del README)")
    void shouldRejectEmailWithoutDot() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@emailcom")         // FALTA . - VIOLACIÓN DE REQUISITO
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidEmailException.class)
          .hasMessageContaining(".");
    }

    @Test
    @DisplayName("Debe rechazar email sin @ ni . (Requisito del README)")
    void shouldRejectEmailWithoutAtAndDot() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pabloemailcom")          // FALTA @ Y . - VIOLACIÓN DE REQUISITO
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    @DisplayName("Debe aceptar email válido con múltiples subdominios")
    void shouldAcceptValidEmailWithMultipleSubdomains() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@subdomain.example.com")  // Válido
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getEmail()).isEqualTo("pablo@subdomain.example.com");
    }
  }

  @Nested
  @DisplayName("Validar DNI")
  class ValidateDni {

    @Test
    @DisplayName("Debe rechazar DNI vacío")
    void shouldRejectEmptyDni() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidDniException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("Debe rechazar DNI nulo")
    void shouldRejectNullDni() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni(null)
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidDniException.class)
          .hasMessageContaining("empty");
    }

    @Test
    @DisplayName("Debe aceptar DNI válido (validación contra API externa es en application layer)")
    void shouldAcceptValidDni() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getDni()).isEqualTo("23454234W");
    }
  }

  @Nested
  @DisplayName("Validar rol de usuario")
  class ValidateRole {

    @Test
    @DisplayName("Debe aceptar rol ADMIN")
    void shouldAcceptAdminRole() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Debe aceptar rol SUPERADMIN")
    void shouldAcceptSuperadminRole() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone("677998899")
          .role("superadmin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getRole()).isEqualTo(UserRole.SUPERADMIN);
    }

    @Test
    @DisplayName("Debe aceptar rol en mayúsculas/minúsculas (case-insensitive)")
    void shouldAcceptRoleCase() {
      // Arrange & Act
      User userUpperCase = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("ADMIN")
          .roomId(1L)
          .build();

      User userLowerCase = User.builder()
          .name("juan")
          .email("juan@example.com")
          .dni("12345678A")
          .phone("677998899")
          .role("superadmin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(userUpperCase.getRole()).isEqualTo(UserRole.ADMIN);
      assertThat(userLowerCase.getRole()).isEqualTo(UserRole.SUPERADMIN);
    }

    @Test
    @DisplayName("Debe rechazar rol vacío")
    void shouldRejectEmptyRole() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidRoleException.class);
    }

    @Test
    @DisplayName("Debe rechazar rol nulo")
    void shouldRejectNullRole() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role(null)
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidRoleException.class);
    }

    @Test
    @DisplayName("Debe rechazar rol inválido (Requisito: solo admin o superadmin)")
    void shouldRejectInvalidRole() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("user")                   // INVÁLIDO - VIOLACIÓN DE REQUISITO
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidRoleException.class);
    }

    @Test
    @DisplayName("Debe rechazar rol moderator")
    void shouldRejectModeratorRole() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("moderator")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidRoleException.class);
    }
  }

  @Nested
  @DisplayName("Validar teléfono según rol")
  class ValidatePhoneByRole {

    @Test
    @DisplayName("Debe permitir ADMIN sin teléfono")
    void shouldAllowAdminWithoutPhone() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)                    // Opcional para ADMIN
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getPhone()).isNull();
      assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Debe permitir ADMIN con teléfono")
    void shouldAllowAdminWithPhone() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone("677998899")             // Opcional pero permitido
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getPhone()).isEqualTo("677998899");
    }

    @Test
    @DisplayName("Debe requerir teléfono para SUPERADMIN (Requisito: phone obligatorio)")
    void shouldRequirePhoneForSuperadmin() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)                    // FALTA PHONE - VIOLACIÓN REQUISITO
          .role("superadmin")             // SUPERADMIN REQUIERE PHONE
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidPhoneException.class)
          .hasMessageContaining("required");
    }

    @Test
    @DisplayName("Debe requerir teléfono no vacío para SUPERADMIN")
    void shouldRequireNonEmptyPhoneForSuperadmin() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone("")                      // VACÍO - VIOLACIÓN REQUISITO
          .role("superadmin")
          .roomId(1L)
          .build())
          .isInstanceOf(InvalidPhoneException.class);
    }

    @Test
    @DisplayName("Debe aceptar teléfono válido para SUPERADMIN")
    void shouldAcceptValidPhoneForSuperadmin() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone("677998899")             // Válido
          .role("superadmin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getPhone()).isEqualTo("677998899");
    }
  }

  @Nested
  @DisplayName("Validar room ID")
  class ValidateRoomId {

    @Test
    @DisplayName("Debe aceptar room ID positivo")
    void shouldAcceptPositiveRoomId() {
      // Arrange & Act
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build();

      // Assert
      assertThat(user.getRoomId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Debe rechazar room ID cero")
    void shouldRejectZeroRoomId() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(0L)
          .build())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("positive");
    }

    @Test
    @DisplayName("Debe rechazar room ID negativo")
    void shouldRejectNegativeRoomId() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(-1L)
          .build())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("positive");
    }

    @Test
    @DisplayName("Debe rechazar room ID nulo")
    void shouldRejectNullRoomId() {
      // Arrange & Act & Assert
      assertThatThrownBy(() -> User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(null)
          .build())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("positive");
    }
  }

  @Nested
  @DisplayName("Comportamiento de notificación según rol")
  class NotificationChannel {

    @Test
    @DisplayName("Debe retornar EMAIL como canal de notificación para ADMIN")
    void shouldReturnEmailChannelForAdmin() {
      // Arrange
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .build();

      // Act
      UserRole.NotificationChannel channel = user.getNotificationChannel();

      // Assert
      assertThat(channel).isEqualTo(UserRole.NotificationChannel.EMAIL);
    }

    @Test
    @DisplayName("Debe retornar SMS como canal de notificación para SUPERADMIN")
    void shouldReturnSmsChannelForSuperadmin() {
      // Arrange
      User user = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone("677998899")
          .role("superadmin")
          .roomId(1L)
          .build();

      // Act
      UserRole.NotificationChannel channel = user.getNotificationChannel();

      // Assert
      assertThat(channel).isEqualTo(UserRole.NotificationChannel.SMS);
    }
  }
}


