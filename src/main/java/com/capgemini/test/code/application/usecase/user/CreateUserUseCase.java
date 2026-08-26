package com.capgemini.test.code.application.usecase.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.ports.input.CreateUserInputPort;
import com.capgemini.test.code.application.ports.output.DniValidationPort;
import com.capgemini.test.code.application.ports.output.NotificationPort;
import com.capgemini.test.code.domain.user.exceptions.DuplicateEmailException;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.user.repository.UserRepository;

/**
 * Caso de uso: Crear un nuevo usuario.
 *
 * Orquestación completa del proceso de creación de usuario:
 * 1. Validar datos de dominio (User.builder().build()) → excepciones DomainException
 * 2. Validar DNI en API externa (fail-fast) → InvalidDniException
 * 3. Validar email no duplicado en BD → DuplicateEmailException
 * 4. Guardar usuario en BD → User.id asignado
 * 5. Notificar creación según rol → EmailPort o SmsPort
 * 6. Retornar respuesta con ID
 *
 * Patrón: Use Case sin anotaciones Spring (@Component, @Transactional)
 * - Java puro, inyección por constructor
 * - Dependencias: UserRepository, DniValidationPort, NotificationPort
 * - Implementado por Spring a través de ApplicationConfig
 * - Trabaja solo con UserDTO (agnóstico de cómo llegan/se envían los datos)
 *
 * Excepciones manejadas:
 * - InvalidUserNameException, InvalidEmailException, etc. → User.builder()
 * - InvalidDniException → DniValidationPort
 * - DuplicateEmailException → validación de aplicación
 * - Exception → NotificationPort (sin capturar, sube)
 * - DataIntegrityViolationException → si la sala 1 no existe (FK constraint)
 *
 * NOTA: Transacción manejada por ApplicationService (@Transactional),
 * no en este Use Case (agnóstico de Spring).
 */
public class CreateUserUseCase implements CreateUserInputPort {

  private final UserRepository userRepository;
  private final DniValidationPort dniValidationPort;
  private final NotificationPort notificationPort;

  /**
   * Constructor que inyecta las dependencias requeridas.
   *
   * @param userRepository puerto de acceso a datos de usuarios
   * @param dniValidationPort puerto para validar DNI en API externa
   * @param notificationPort puerto para notificar creación del usuario
   */
  public CreateUserUseCase(
      UserRepository userRepository,
      DniValidationPort dniValidationPort,
      NotificationPort notificationPort) {
    this.userRepository = userRepository;
    this.dniValidationPort = dniValidationPort;
    this.notificationPort = notificationPort;
  }

  /**
   * Ejecuta el caso de uso de crear un usuario con validaciones ordenadas (fail-fast).
   *
   * Flujo:
   * 1. Crear objeto User con validaciones de dominio
   * 2. Validar DNI en API externa
   * 3. Verificar email no duplicado
   * 4. Guardar en BD
   * 5. Enviar notificación
   * 6. Retornar respuesta con ID
   *
   * @param userDTO DTO de aplicación con datos del usuario (name, email, dni, phone, rol)
   * @return UserDTO con ID, name, email, rol, roomId
   * @throws InvalidUserNameException si nombre inválido (vacío o > 6 caracteres)
   * @throws InvalidEmailException si email inválido (vacío, sin @, sin .)
   * @throws InvalidDniException si DNI inválido (vacío)
   * @throws InvalidPhoneException si phone requerido pero vacío (para SUPERADMIN)
   * @throws InvalidRoleException si rol no es ADMIN ni SUPERADMIN
   * @throws InvalidDniException si API externa rechaza el DNI
   * @throws DuplicateEmailException si email ya existe en BD
   * @throws Exception si notificación falla (causa ROLLBACK si hay transacción)
   * @throws DataIntegrityViolationException si sala 1 no existe (FK constraint)
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
        .roomId(1L)  // Se asume que sala 1 siempre existe
        .build();

    // PASO 2: Validar DNI en API externa (fail-fast, antes de tocar BD)
    dniValidationPort.validate(user.getDni());

    // PASO 3: Validar email no duplicado
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new DuplicateEmailException(user.getEmail());
    }

    // PASO 4: Guardar usuario en BD (asigna ID)
    User savedUser = userRepository.save(user);

    // PASO 5: Notificar creación (best-effort, si falla, sube excepción)
    notificationPort.notifyUserCreated(savedUser);

    // PASO 6: Retornar DTO con datos del usuario creado
    return UserDTO.builder()
        .id(savedUser.getId())
        .name(savedUser.getName())
        .email(savedUser.getEmail())
        .phone(savedUser.getPhone())
        .dni(savedUser.getDni())
        .rol(savedUser.getRole().toString())
        .roomId(savedUser.getRoomId())
        .build();
  }
}
