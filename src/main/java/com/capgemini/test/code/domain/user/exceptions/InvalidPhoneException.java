package com.capgemini.test.code.domain.user.exceptions;

import com.capgemini.test.code.domain.exceptions.DomainException;

public class InvalidPhoneException extends DomainException {
  public InvalidPhoneException(String message) {
    super(message, "INVALID_PHONE", 409);
  }
}

