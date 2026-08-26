package com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para obtener un usuario (HTTP layer).
 *
 * Representa la respuesta HTTP que se envía al cliente tras obtener un usuario.
 * Mapea directamente al JSON de respuesta.
 *
 * Ubicación: Infrastructure Layer (REST Adapter)
 * Se construye desde UserDTO en UserRestMapper
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {

  /** ID del usuario */
  private Long id;

  /** Nombre del usuario */
  private String name;

  /** Email del usuario */
  private String email;

  /** Teléfono del usuario */
  private String phone;

  /** DNI del usuario */
  private String dni;

  /** Rol del usuario */
  private String rol;

  /** ID de la sala a la que pertenece */
  private Long roomId;
}

