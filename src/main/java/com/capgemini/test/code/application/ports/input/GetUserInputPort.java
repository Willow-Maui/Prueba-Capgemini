package com.capgemini.test.code.application.ports.input;

import com.capgemini.test.code.application.dto.UserDTO;

/**
 * Puerto de entrada (interfaz) para obtener un usuario.
 * Define el contrato que el UseCase de lectura de usuario debe cumplir.
 *
 * Implementación: GetUserUseCase
 */
public interface GetUserInputPort {

  /**
   * Ejecuta el caso de uso de obtener un usuario por su ID.
   *
   * Validaciones:
   * 1. Buscar usuario por ID en la sala 1
   * 2. Validar que existe y está en la sala 1
   * 3. Mapear a UserDTO
   * 4. Retornar DTO
   *
   * @param userId el ID del usuario a obtener
   * @return DTO con los datos del usuario
   * @throws UserNotFoundException si el usuario no existe o no está en sala 1
   */
  UserDTO execute(Long userId);
}

