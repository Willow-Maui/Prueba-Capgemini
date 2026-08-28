package com.capgemini.test.code.application.ports.output;

import com.capgemini.test.code.application.dto.UserDTO;


/**
 * Puerto de salida (interfaz) para notificaciones de Usuario.
 * Define el contrato que debe cumplir cualquier adaptador de notificación.
 *
 * Implementación concreta: NotificationAdapter (usa Feign clients)
 *
 * APIs externas (MockServer):
 * - POST http://localhost:1080/email (para ADMIN)
 * - POST http://localhost:1080/sms (para SUPERADMIN)
 */
public interface NotificationPort {

  /**
   * Notifica al usuario sobre su creación.
   * Envía email si el rol es ADMIN, SMS si es SUPERADMIN.
   *
   * El mensaje es siempre: "usuario guardado"
   *
   * @param user el usuario que fue creado
   * @throws Exception si el envío de notificación falla
   *                   (NOTA: según requisitos, esto causa ROLLBACK de la transacción)
   */
  void notifyUserCreated(UserDTO user);
}

