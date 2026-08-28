package com.capgemini.test.code.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para lectura y manipulación de datos de usuario internamente en la capa de aplicación.
 *
 * Se usa como objeto intermedio entre:
 * - Entrada de REST → Application (CreateUserRequest)
 * - Application → Domain (User)
 * - Domain → Application (User)
 * - Application → REST (CreateUserResponse, UserResponse)
 *
 * Es agnóstico del mapeo específico REST o BD, solo contiene datos de negocio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  /** ID del usuario (asignado por BD) */
  private Long id;

  /** Nombre del usuario */
  private String name;

  /** Email del usuario (único) */
  private String email;

  /** Teléfono del usuario */
  private String phone;

  /** DNI del usuario */
  private String dni;

  /** Rol del usuario (ADMIN o SUPERADMIN) */
  private String role;

  /** ID de la sala a la que pertenece */
  private Long roomId;
}

