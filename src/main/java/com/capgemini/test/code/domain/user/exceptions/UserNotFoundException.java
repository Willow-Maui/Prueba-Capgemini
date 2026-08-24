package com.capgemini.test.code.domain.user.exceptions;

import com.capgemini.test.code.domain.exceptions.DomainException;

/**
 * Excepción lanzada cuando un usuario no es encontrado en la persistencia.
 * HTTP 404 Not Found.
 */
public class UserNotFoundException extends DomainException {
  public UserNotFoundException(Long userId) {
    super("User not found with ID: " + userId, "USER_NOT_FOUND", 404);
  }

  public UserNotFoundException(Long userId, Long roomId) {
    super("User not found with ID: " + userId + " in room: " + roomId, "USER_NOT_FOUND", 404);
  }
}

