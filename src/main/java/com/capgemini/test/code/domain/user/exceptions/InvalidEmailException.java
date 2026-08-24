package com.capgemini.test.code.domain.user.exceptions;

import com.capgemini.test.code.domain.exceptions.DomainException;

public class InvalidEmailException extends DomainException {
  public InvalidEmailException(String message) {
    super(message, "INVALID_EMAIL", 409);
  }
}

