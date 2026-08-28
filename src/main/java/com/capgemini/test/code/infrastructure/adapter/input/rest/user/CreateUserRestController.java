package com.capgemini.test.code.infrastructure.adapter.input.rest.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.ports.input.CreateUserInputPort;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserRequest;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserResponse;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.mapper.UserRestMapper;
import com.capgemini.test.code.infrastructure.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller para crear usuarios.
 *
 * Responsabilidad:
 * - Recibir solicitud HTTP POST /api/v1/users
 * - Validar JSON y mapear a DTO de aplicación
 * - Ejecutar Use Case de creación
 * - Mapear respuesta a DTO REST
 * - Retornar HTTP 201 o 409
 *
 * Patrón: Controller siguiendo Hexagonal Architecture
 * - Input Adapter (recibe HTTP)
 * - Usa Application Layer (puertos, use cases)
 * - Mapeos explícitos (RestRequest → DTO → RestResponse)
 *
 * Excepciones:
 * - InvalidUserNameException → 409 Conflict
 * - InvalidEmailException → 409 Conflict
 * - InvalidDniException → 409 Conflict
 * - InvalidPhoneException → 409 Conflict
 * - InvalidRoleException → 409 Conflict
 * - DuplicateEmailException → 409 Conflict
 * (Manejadas por GlobalExceptionHandler)
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class CreateUserRestController {

    private final CreateUserInputPort createUserUseCase;

    /**
     * POST /api/v1/users
     *
     * Crea un nuevo usuario en la sala 1.
     *
     * Request:
     * ```json
     * {
     *   "name": "pablo",
     *   "email": "email@email.com",
     *   "phone": "677998899",
     *   "rol": "admin",
     *   "dni": "23454234W"
     * }
     * ```
     *
     * Response 201 Created:
     * ```json
     * {
     *   "id": 1,
     *   "name": "pablo",
     *   "email": "email@email.com",
     *   "rol": "admin",
     *   "roomId": 1
     * }
     * ```
     *
     * Response 409 Conflict:
     * ```json
     * {
     *   "code": 409,
     *   "message": "error validation <field>"
     * }
     * ```
     *
     * @param request DTO REST con datos del usuario
     * @return ResponseEntity con status 201 y CreateUserResponse
     * @throws InvalidUserNameException si nombre inválido
     * @throws InvalidEmailException si email inválido
     * @throws InvalidDniException si DNI rechazado
     * @throws DuplicateEmailException si email ya existe
     */
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        // Mapear desde REST DTO a Application DTO
        UserDTO userDTO = UserRestMapper.toApplicationDTO(request);

        // Ejecutar Use Case
        UserDTO createdUser = createUserUseCase.execute(userDTO);

        // Mapear desde Application DTO a REST Response
        CreateUserResponse response = UserRestMapper.toHttpResponse(createdUser);

        // Retornar 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}



