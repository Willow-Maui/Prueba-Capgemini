package com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la creación de usuarios (HTTP layer).
 *
 * Representa la respuesta HTTP que se envía al cliente tras crear un usuario.
 * Mapea directamente al JSON de respuesta.
 *
 * Ubicación: Infrastructure Layer (REST Adapter)
 * Se construye desde CreateUserResponseApplication en UserRestMapper
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {

  /** ID del usuario creado (asignado por la base de datos) */
  private Long id;

  /** Nombre del usuario creado */
  private String name;

  /** Email del usuario creado */
  private String email;

  /** Rol del usuario creado */
  private String rol;

  /** ID de la sala a la que pertenece el usuario */
  private Long roomId;
}

