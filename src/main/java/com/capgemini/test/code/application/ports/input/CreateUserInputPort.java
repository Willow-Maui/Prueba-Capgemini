package com.capgemini.test.code.application.ports.input;

import com.capgemini.test.code.application.dto.UserDTO;

/**
 * Puerto de entrada para el caso de uso: Crear un nuevo usuario.
 *
 * Define el contrato que especifica cómo se ejecuta la creación de usuario
 * desde la capa de aplicación.
 *
 * Implementación: CreateUserUseCase
 *
 * Responsabilidad: Orquestar el flujo de creación de usuario
 * (validaciones, persistencia, notificaciones).
 *
 * Patrón: El Use Case recibe UserDTO (agnóstico de cómo llegan los datos).
 * Los mapeos desde/hacia DTOs REST ocurren en Infrastructure Layer.
 */
public interface CreateUserInputPort {

  /**
   * Ejecuta el caso de uso de crear un usuario.
   *
   * Flujo de validaciones (fail-fast):
   * 1. Validar dominio (User.builder().build())
   * 2. Validar DNI en API externa
   * 3. Validar email duplicado en BD
   * 4. Guardar usuario en BD
   * 5. Notificar creación según rol
   *
   * @param userDTO DTO de aplicación con datos del usuario (name, email, dni, phone, rol)
   * @return UserDTO con ID y datos del usuario creado
   * @throws InvalidUserNameException si nombre no cumple validaciones
   * @throws InvalidEmailException si email no cumple validaciones
   * @throws InvalidDniException si DNI no es válido
   * @throws InvalidPhoneException si phone requerido pero vacío
   * @throws InvalidRoleException si rol no es válido
   * @throws DuplicateEmailException si email ya existe en BD
   * @throws Exception si la notificación falla (causará ROLLBACK si hay transacción)
   */
  UserDTO execute(UserDTO userDTO);
}

