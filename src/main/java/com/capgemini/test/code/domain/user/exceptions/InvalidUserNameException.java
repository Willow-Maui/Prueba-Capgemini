package com.capgemini.test.code.domain.user.exceptions;

import com.capgemini.test.code.domain.exceptions.DomainException;

/**
 * Excepción lanzada cuando el nombre de usuario es inválido.
 */
public class InvalidUserNameException extends DomainException {
  public InvalidUserNameException(String message) {
    super(message, "INVALID_USER_NAME", 409);
  }
}

