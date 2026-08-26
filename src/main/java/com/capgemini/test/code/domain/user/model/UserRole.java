package com.capgemini.test.code.domain.user.model;

/**
 * Rol de usuario en el dominio.
 * 
 * Define los roles posibles para un usuario y su correspondiente canal de notificación.
 * Es un enum separado de la entidad User, siguiendo el patrón de DDD.
 * 
 * Los roles encapsulan comportamiento de negocio (estrategia de notificación).
 * 
 * Roles disponibles:
 * - ADMIN: Usuario administrador, se notifica por EMAIL
 * - SUPERADMIN: Super administrador, se notifica por SMS (phone obligatorio)
 */
public enum UserRole {

  /**
   * Rol administrador.
   * Canal de notificación: EMAIL
   * Mensaje: "usuario guardado"
   * Phone: Opcional
   */
  ADMIN("ADMIN") {
    @Override
    public NotificationChannel getNotificationChannel() {
      return NotificationChannel.EMAIL;
    }
  },

  /**
   * Rol super administrador.
   * Canal de notificación: SMS
   * Mensaje: "usuario guardado"
   * Phone: Obligatorio
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

  /**
   * Obtiene el valor string del rol.
   * 
   * @return valor del rol (ADMIN, SUPERADMIN)
   */
  public String getValue() {
    return value;
  }

  /**
   * Obtiene el canal de notificación según el rol.
   * Implementado por cada rol específico.
   * 
   * @return canal de notificación (EMAIL o SMS)
   */
  public abstract NotificationChannel getNotificationChannel();

  /**
   * Canales de notificación disponibles en el sistema.
   * Estrategia de notificación según el rol.
   */
  public enum NotificationChannel {
    /** Notificación vía correo electrónico */
    EMAIL,
    /** Notificación vía SMS */
    SMS
  }
}

