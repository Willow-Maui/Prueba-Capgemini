package com.capgemini.test.code.infrastructure.adapter.input.rest.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.ports.input.GetUserInputPort;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.GetUserResponse;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.mapper.UserRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller para obtener usuarios.
 *
 * Responsabilidad:
 * - Recibir solicitud HTTP GET /api/v1/users/{id}
 * - Ejecutar Use Case de lectura
 * - Mapear respuesta a DTO REST
 * - Retornar HTTP 200 o 404
 *
 * Patrón: Controller siguiendo Hexagonal Architecture
 * - Input Adapter (recibe HTTP)
 * - Usa Application Layer (puertos, use cases)
 * - Mapeos explícitos (DTO → RestResponse)
 *
 * Excepciones:
 * - UserNotFoundException → 404 Not Found
 * (Manejadas por GlobalExceptionHandler)
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class GetUserRestController {

    private final GetUserInputPort getUserUseCase;

    /**
     * GET /api/v1/users/{id}
     *
     * Obtiene un usuario por su ID en la sala 1.
     *
     * Response 200 OK:
     * ```json
     * {
     *   "id": 1,
     *   "name": "pablo",
     *   "email": "email@email.com",
     *   "phone": "677998899",
     *   "dni": "23454234W",
     *   "rol": "admin",
     *   "roomId": 1
     * }
     * ```
     *
     * Response 404 Not Found:
     * ```json
     * {
     *   "code": 404,
     *   "message": "User not found with id: <id>"
     * }
     * ```
     *
     * @param id el ID del usuario a obtener
     * @return ResponseEntity con status 200 y GetUserResponse
     * @throws UserNotFoundException si el usuario no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable Long id) {
        // Ejecutar Use Case
        UserDTO user = getUserUseCase.execute(id);

        // Mapear desde Application DTO a REST Response
        GetUserResponse response = UserRestMapper.toGetUserResponse(user);

        // Retornar 200 OK
        return ResponseEntity.ok(response);
    }
}

