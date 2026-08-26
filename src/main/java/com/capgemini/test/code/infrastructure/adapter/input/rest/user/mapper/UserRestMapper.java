package com.capgemini.test.code.infrastructure.adapter.input.rest.user.mapper;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserRequest;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserResponse;

/**
 * Mapper para convertir entre DTOs REST (HTTP) y DTOs de Aplicación.
 *
 * Responsabilidad:
 * - Convertir CreateUserRequest (HTTP) → UserDTO (Application)
 * - Convertir UserDTO (Application) → CreateUserResponse (HTTP)
 *
 * Ubicación: Infrastructure Layer - REST Adapter
 * Este mapper es la frontera entre HTTP y Application Layer.
 *
 * Patrón: Mapper manual (no usa MapStruct en este caso por simplicidad)
 * Se podría usar MapStruct para mapeos más complejos en el futuro.
 */
public class UserRestMapper {

  /**
   * Convierte un DTO REST de entrada a un DTO de aplicación.
   * Se usa para transformar la solicitud HTTP al formato que espera la capa de aplicación.
   *
   * @param restRequest DTO de entrada REST (no nulo)
   * @return UserDTO para procesamiento en application layer
   */
  public static UserDTO toApplicationDTO(CreateUserRequest restRequest) {
    if (restRequest == null) {
      return null;
    }
    return UserDTO.builder()
        .name(restRequest.getName())
        .email(restRequest.getEmail())
        .phone(restRequest.getPhone())
        .dni(restRequest.getDni())
        .rol(restRequest.getRol())
        .build();
  }

  /**
   * Convierte un DTO de aplicación a un DTO REST de salida.
   * Se usa para transformar la respuesta de aplicación al formato HTTP.
   *
   * @param applicationDTO DTO de respuesta de aplicación (no nulo)
   * @return CreateUserResponse para enviar al cliente HTTP
   */
  public static CreateUserResponse toHttpResponse(UserDTO applicationDTO) {
    if (applicationDTO == null) {
      return null;
    }
    return CreateUserResponse.builder()
        .id(applicationDTO.getId())
        .name(applicationDTO.getName())
        .email(applicationDTO.getEmail())
        .rol(applicationDTO.getRol())
        .roomId(applicationDTO.getRoomId())
        .build();
  }
}

