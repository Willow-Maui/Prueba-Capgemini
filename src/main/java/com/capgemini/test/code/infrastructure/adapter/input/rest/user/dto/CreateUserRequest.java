package com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación de usuarios (HTTP layer).
 *
 * Representa los datos que llegan en la solicitud HTTP POST /users
 * Mapea directamente desde el JSON del cliente.
 *
 * Ubicación: Infrastructure Layer (REST Adapter)
 * Se transforma a CreateUserRequest en UserRestMapper
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

  /** Nombre del usuario (máximo 6 caracteres) */
  private String name;

  /** Email del usuario (debe contener @ y .) */
  private String email;

  /** DNI del usuario (validado contra API externa) */
  private String dni;

  /** Teléfono del usuario (obligatorio para SUPERADMIN) */
  private String phone;

  /** Rol del usuario (ADMIN o SUPERADMIN) */
  private String rol;
}

