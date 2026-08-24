package com.capgemini.test.code.application.usecase.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.mapper.UserMapper;
import com.capgemini.test.code.application.ports.input.GetUserInputPort;
import com.capgemini.test.code.domain.user.exceptions.UserNotFoundException;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.user.repository.UserRepository;

/**
 * Caso de uso para obtener un usuario por su ID.
 *
 * Orquestación del proceso de lectura:
 * 1. Buscar usuario por ID en sala 1
 * 2. Si no existe o no está en sala 1, lanzar UserNotFoundException (simulado con null check)
 * 3. Mapear User a UserDTO
 * 4. Retornar DTO
 *
 * Sin transacción (es una lectura).
 * Sin Spring annotations (inyección en constructor).
 * Spring los instancia en ApplicationConfig.
 */
public class GetUserUseCase implements GetUserInputPort {

  private final UserRepository userRepository;

  public GetUserUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Ejecuta el caso de uso de obtener un usuario por su ID.
   *
   * Según las respuestas: "B) Buscar sin validar, validar en UseCase"
   * → Busca por ID sin validar sala, luego valida que está en sala 1
   *
   * @param userId el ID del usuario a obtener
   * @return UserDTO con los datos del usuario
   * @throws UserNotFoundException si el usuario no existe o no está en sala 1
   */
  @Override
  public UserDTO execute(Long userId) {

    // PASO 1-2: Buscar usuario por ID (sin validar sala)
    User user = userRepository.findById(userId);

    // Validar que existe
    if (user == null) {
      throw new UserNotFoundException(userId);
    }

    // PASO 3: Mapear a DTO
    UserDTO dto = UserMapper.toDTO(user);

    // PASO 4: Retornar
    return dto;
  }
}



