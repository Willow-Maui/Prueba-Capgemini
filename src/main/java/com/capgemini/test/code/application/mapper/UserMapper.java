package com.capgemini.test.code.application.mapper;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.domain.user.model.User;

/**
 * Mapper para convertir entre User (dominio) y UserDTO (presentación).
 */
public class UserMapper {

  /**
   * Convierte una entidad User a un DTO UserDTO.
   */
  public static UserDTO toDTO(User user) {
    if (user == null) {
      return null;
    }
    return new UserDTO(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getPhone(),
        user.getDni(),
        user.getRole().getValue(),
        user.getRoomId()
    );
  }
}

