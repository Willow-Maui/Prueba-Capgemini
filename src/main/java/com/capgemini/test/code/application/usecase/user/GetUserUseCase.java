package com.capgemini.test.code.application.usecase.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.mapper.UserMapper;
import com.capgemini.test.code.application.ports.input.GetUserInputPort;
import com.capgemini.test.code.domain.user.exceptions.UserNotFoundException;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.user.repository.UserRepository;

/**
 * Caso de uso: Obtener un usuario por su ID.
 *
 * Orquestación del proceso de lectura:
 * 1. Buscar usuario por ID en repositorio
 * 2. Si no existe, lanzar UserNotFoundException
 * 3. Mapear User a UserDTO
 * 4. Retornar DTO
 *
 * Patrón: Use Case sin anotaciones Spring
 * - Java puro, inyección por constructor
 * - Dependencia: UserRepository
 * - Implementado por Spring en ApplicationConfig
 *
 * Características:
 * - Sin transacción (es una lectura pura)
 * - Sin efectos secundarios
 * - Agnóstico de Spring
 */
public class GetUserUseCase implements GetUserInputPort {

  private final UserRepository userRepository;

  /**
   * Constructor que inyecta la dependencia de repositorio.
   *
   * @param userRepository puerto de acceso a datos de usuarios
   */
  public GetUserUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Ejecuta el caso de uso de obtener un usuario por su ID.
   *
   * Flujo:
   * 1. Buscar usuario por ID
   * 2. Validar que existe (si no, lanzar excepción)
   * 3. Mapear a DTO
   * 4. Retornar
   *
   * @param userId el ID del usuario a obtener (no nulo)
   * @return UserDTO con los datos completos del usuario
   * @throws UserNotFoundException si el usuario no existe
   */
  @Override
  public UserDTO execute(Long userId) {

    // PASO 1: Buscar usuario por ID
    User user = userRepository.findById(userId);

    // PASO 2: Validar que existe
    if (user == null) {
      throw new UserNotFoundException(userId);
    }

    // PASO 3: Mapear a DTO
    UserDTO dto = UserMapper.toDTO(user);

    // PASO 4: Retornar
    return dto;
  }
}
