package com.capgemini.test.code.domain.user.exceptions;

import com.capgemini.test.code.domain.exceptions.DomainException;

public class InvalidRoleException extends DomainException {
  public InvalidRoleException(String role) {
    super("Role '" + role + "' is invalid. Must be ADMIN or SUPERADMIN", "INVALID_ROLE", 409);
  }
}

