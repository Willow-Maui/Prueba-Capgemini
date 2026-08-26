package com.capgemini.test.code.application.mapper;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.domain.user.model.User;

/**
 * Mapper para convertir entre User (dominio) y UserDTO (aplicación).
 *
 * Responsabilidad:
 * - Convertir User → UserDTO (lectura)
 * - Convertir UserDTO ↔ User (preparación/respuesta)
 *
 * Ubicación: Application Layer (agnóstico de cómo llegan los datos)
 * Los mapeos desde/hacia DTOs REST ocurren en Infrastructure Layer (UserRestMapper).
 *
 * Patrón: Mapper manual (no usa MapStruct en este caso por simplicidad)
 * Se podría usar MapStruct para mapeos más complejos en el futuro.
 */
public class UserMapper {

  /**
   * Convierte una entidad User a un DTO UserDTO.
   * Se usa internamente en la aplicación para manipular datos.
   *
   * @param user entidad de dominio (no nulo)
   * @return UserDTO con todos los datos del usuario
   */
  public static UserDTO toDTO(User user) {
    if (user == null) {
      return null;
    }
    return UserDTO.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .phone(user.getPhone())
        .dni(user.getDni())
        .rol(user.getRole().name())
        .roomId(user.getRoomId())
        .build();
  }
}

