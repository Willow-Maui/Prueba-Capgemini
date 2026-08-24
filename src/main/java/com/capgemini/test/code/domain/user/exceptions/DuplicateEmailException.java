package com.capgemini.test.code.domain.user.exceptions;

import com.capgemini.test.code.domain.exceptions.DomainException;

public class DuplicateEmailException extends DomainException {
  public DuplicateEmailException(String email) {
    super("Email " + email + " already exists", "DUPLICATE_EMAIL", 409);
  }
}

