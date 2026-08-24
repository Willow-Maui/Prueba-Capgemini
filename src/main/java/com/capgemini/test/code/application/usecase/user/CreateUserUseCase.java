package com.capgemini.test.code.application.usecase.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.ports.input.CreateUserInputPort;
import com.capgemini.test.code.application.ports.output.DniValidationPort;
import com.capgemini.test.code.application.ports.output.NotificationPort;
import com.capgemini.test.code.domain.user.exceptions.DuplicateEmailException;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.user.repository.UserRepository;

/**
 * Caso de uso para crear un usuario.
 *
 * Orquestación completa del proceso de creación:
 * 1. Validar dominio (User.builder().build())
 * 2. Validar DNI en API externa (fail-fast)
 * 3. Validar email no duplicado
 * 4. Guardar en BD (asume sala 1 existe)
 * 5. Notificar creación
 * 6. Retornar respuesta con ID
 *
 * AGNÓSTICO de Spring: Sin anotaciones Spring.
 * La transacción es manejada por UserApplicationService (@Transactional en el método).
 *
 * NOTA: Se asume que la sala 1 siempre existe. Si falla por FK constraint,
 * la excepción de BD sube sin ser capturada.
 *
 * Excepciones:
 * - InvalidUserNameException, InvalidEmailException, etc. → del User.builder()
 * - InvalidDniException → de DniValidationPort
 * - DuplicateEmailException → validación de aplicación
 * - Exception genérica → de NotificationPort
 * - DataIntegrityViolationException → si FK constraint falla
 *
 * Constructor inyecta dependencias (puro).
 * Spring los instancia en ApplicationConfig.
 */
public class CreateUserUseCase implements CreateUserInputPort {

  private final UserRepository userRepository;
  private final DniValidationPort dniValidationPort;
  private final NotificationPort notificationPort;

  public CreateUserUseCase(
      UserRepository userRepository,
      DniValidationPort dniValidationPort,
      NotificationPort notificationPort) {
    this.userRepository = userRepository;
    this.dniValidationPort = dniValidationPort;
    this.notificationPort = notificationPort;
  }

  /**
   * Ejecuta el caso de uso de crear un usuario con validaciones ordenadas.
   *
   * @param userDTO DTO con los datos del usuario a crear
   * @return UserDTO con el ID del usuario creado
   * @throws InvalidUserNameException si el nombre no es válido
   * @throws InvalidEmailException si el email no es válido
   * @throws InvalidDniException si el DNI es rechazado por la API
   * @throws InvalidPhoneException si phone es inválido para el rol
   * @throws InvalidRoleException si el rol no es válido
   * @throws DuplicateEmailException si el email ya existe en la BD
   * @throws Exception si la notificación falla (causa ROLLBACK)
   * @throws DataIntegrityViolationException si la sala no existe (FK constraint)
   */
  @Override
  public UserDTO execute(UserDTO userDTO) {

    // PASO 1: Validar dominio (crea User y valida todos sus campos)
    User user = User.builder()
        .name(userDTO.getName())
        .email(userDTO.getEmail())
        .dni(userDTO.getDni())
        .phone(userDTO.getPhone())
        .role(userDTO.getRol())
        .roomId(1L)
        .build();

    // PASO 2: Validar DNI en API externa (fail-fast)
    dniValidationPort.validate(user.getDni());

    // PASO 3: Validar email no duplicado
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new DuplicateEmailException("Email already exists: " + user.getEmail());
    }

    // PASO 4: Guardar usuario en BD
    User savedUser = userRepository.save(user);

    // PASO 5: Notificar creación
    notificationPort.notifyUserCreated(savedUser);

    // PASO 6: Retornar DTO con ID
    UserDTO result = new UserDTO();
    result.setId(savedUser.getId());
    result.setName(savedUser.getName());
    result.setEmail(savedUser.getEmail());
    result.setPhone(savedUser.getPhone());
    result.setDni(savedUser.getDni());
    result.setRol(savedUser.getRole().toString());
    result.setRoomId(savedUser.getRoomId());
    return result;
  }
}






