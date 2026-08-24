package com.capgemini.test.code.application.ports.input;

import com.capgemini.test.code.application.dto.UserDTO;

/**
 * Puerto de entrada (interfaz) para crear un nuevo usuario.
 * Define el contrato que el UseCase de creación de usuario debe cumplir.
 *
 * Implementación: CreateUserUseCase
 */
public interface CreateUserInputPort {

  /**
   * Ejecuta el caso de uso de crear un usuario.
   *
   * Validaciones en orden (fail-fast):
   * 1. Validar dominio (User.builder().build())
   * 2. Validar DNI en API externa
   * 3. Validar email duplicado
   * 4. Guardar usuario en BD
   * 5. Notificar creación
   *
   * @param userDTO DTO con datos del usuario (name, email, dni, phone, rol)
   * @return userDTO con el ID del usuario creado
   * @throws InvalidUserNameException si el nombre no es válido
   * @throws InvalidEmailException si el email no es válido
   * @throws InvalidDniException si el DNI es rechazado por la API
   * @throws InvalidPhoneException si phone es inválido
   * @throws InvalidRoleException si el rol no es válido
   * @throws DuplicateEmailException si el email ya existe
   * @throws Exception si la notificación falla (causa ROLLBACK)
   */
  UserDTO execute(UserDTO userDTO);
}

