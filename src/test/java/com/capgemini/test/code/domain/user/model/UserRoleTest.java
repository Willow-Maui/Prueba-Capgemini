package com.capgemini.test.code.domain.user.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios para el enum UserRole.
 *
 * Verifica que los canales de notificación sean correctos según el rol.
 *
 * Requisitos del README:
 * - ADMIN: Notificación por EMAIL con mensaje "usuario guardado"
 * - SUPERADMIN: Notificación por SMS con mensaje "usuario guardado"
 */
@DisplayName("UserRole Enum - Canales de Notificación")
class UserRoleTest {

  @Nested
  @DisplayName("Rol ADMIN")
  class AdminRole {

    @Test
    @DisplayName("Debe tener valor 'ADMIN'")
    void shouldHaveAdminValue() {
      // Arrange & Act
      UserRole role = UserRole.ADMIN;

      // Assert
      assertThat(role.getValue()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Debe retornar NotificationChannel.EMAIL (Requisito: admin notifica por email)")
    void shouldReturnEmailNotificationChannel() {
      // Arrange
      UserRole role = UserRole.ADMIN;

      // Act
      UserRole.NotificationChannel channel = role.getNotificationChannel();

      // Assert
      assertThat(channel).isEqualTo(UserRole.NotificationChannel.EMAIL);
    }

    @Test
    @DisplayName("Debe ser diferente a SUPERADMIN")
    void shouldBeDifferentFromSuperadmin() {
      // Arrange
      UserRole admin = UserRole.ADMIN;
      UserRole superadmin = UserRole.SUPERADMIN;

      // Assert
      assertThat(admin).isNotEqualTo(superadmin);
    }
  }

  @Nested
  @DisplayName("Rol SUPERADMIN")
  class SuperadminRole {

    @Test
    @DisplayName("Debe tener valor 'SUPERADMIN'")
    void shouldHaveSuperadminValue() {
      // Arrange & Act
      UserRole role = UserRole.SUPERADMIN;

      // Assert
      assertThat(role.getValue()).isEqualTo("SUPERADMIN");
    }

    @Test
    @DisplayName("Debe retornar NotificationChannel.SMS (Requisito: superadmin notifica por SMS)")
    void shouldReturnSmsNotificationChannel() {
      // Arrange
      UserRole role = UserRole.SUPERADMIN;

      // Act
      UserRole.NotificationChannel channel = role.getNotificationChannel();

      // Assert
      assertThat(channel).isEqualTo(UserRole.NotificationChannel.SMS);
    }

    @Test
    @DisplayName("Debe ser diferente a ADMIN")
    void shouldBeDifferentFromAdmin() {
      // Arrange
      UserRole admin = UserRole.ADMIN;
      UserRole superadmin = UserRole.SUPERADMIN;

      // Assert
      assertThat(superadmin).isNotEqualTo(admin);
    }
  }

  @Nested
  @DisplayName("NotificationChannel Enum")
  class NotificationChannels {

    @Test
    @DisplayName("Debe tener canal EMAIL")
    void shouldHaveEmailChannel() {
      // Assert
      assertThat(UserRole.NotificationChannel.EMAIL).isNotNull();
    }

    @Test
    @DisplayName("Debe tener canal SMS")
    void shouldHaveSmsChannel() {
      // Assert
      assertThat(UserRole.NotificationChannel.SMS).isNotNull();
    }

    @Test
    @DisplayName("EMAIL y SMS deben ser diferentes")
    void shouldEmailAndSmsBeDifferent() {
      // Assert
      assertThat(UserRole.NotificationChannel.EMAIL)
          .isNotEqualTo(UserRole.NotificationChannel.SMS);
    }
  }

  @Nested
  @DisplayName("Conversión desde String")
  class StringConversion {

    @Test
    @DisplayName("Debe convertir 'admin' a UserRole.ADMIN")
    void shouldConvertStringToAdmin() {
      // Act
      UserRole role = UserRole.valueOf("ADMIN");

      // Assert
      assertThat(role).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Debe convertir 'superadmin' a UserRole.SUPERADMIN")
    void shouldConvertStringToSuperadmin() {
      // Act
      UserRole role = UserRole.valueOf("SUPERADMIN");

      // Assert
      assertThat(role).isEqualTo(UserRole.SUPERADMIN);
    }
  }
}

