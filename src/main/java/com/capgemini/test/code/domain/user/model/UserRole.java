package com.capgemini.test.code.domain.user.model;

/**
 * Rol de usuario.
 * Define los roles posibles para un usuario y su correspondiente canal de notificación.
 */
public enum UserRole {

  /**
   * Rol administrador.
   * Se notifica por EMAIL con el mensaje "usuario guardado".
   */
  ADMIN("ADMIN") {
    @Override
    public NotificationChannel getNotificationChannel() {
      return NotificationChannel.EMAIL;
    }
  },

  /**
   * Rol super administrador.
   * Se notifica por SMS con el mensaje "usuario guardado".
   * El phone es obligatorio para este rol.
   */
  SUPERADMIN("SUPERADMIN") {
    @Override
    public NotificationChannel getNotificationChannel() {
      return NotificationChannel.SMS;
    }
  };

  private final String value;

  UserRole(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  /**
   * Obtiene el canal de notificación según el rol.
   */
  public abstract NotificationChannel getNotificationChannel();

  /**
   * Canales de notificación disponibles.
   */
  public enum NotificationChannel {
    EMAIL,
    SMS
  }
}

