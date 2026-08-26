package com.capgemini.test.code.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la creación de usuarios.
 *
 * Representa los datos que retorna el Use Case de creación de usuarios
 * y que se envían como respuesta HTTP al cliente.
 *
 * Contiene el ID asignado por la base de datos tras la creación exitosa.
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

