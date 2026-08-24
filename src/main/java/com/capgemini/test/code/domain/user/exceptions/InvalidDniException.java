package com.capgemini.test.code.domain.user.exceptions;

import com.capgemini.test.code.domain.exceptions.DomainException;

public class InvalidDniException extends DomainException {
  public InvalidDniException(String message) {
    super(message, "INVALID_DNI", 409);
  }
}

