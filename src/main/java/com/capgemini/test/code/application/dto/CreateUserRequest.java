package com.capgemini.test.code.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para la creación de usuarios.
 *
 * Representa los datos que llegan desde la capa de REST y que se pasan
 * al Use Case de creación de usuarios.
 *
 * No incluye validaciones, las validaciones ocurren en la capa de dominio
 * durante la construcción del objeto User.
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

