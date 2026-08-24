package com.capgemini.test.code.domain.exceptions;

/**
 * Excepción base para excepciones de dominio.
 * Las excepciones de dominio representan violaciones de reglas de negocio,
 * no errores técnicos.
 */
public abstract class DomainException extends RuntimeException {

  private final String code;
  private final int httpStatusCode;

  public DomainException(String message, String code, int httpStatusCode) {
    super(message);
    this.code = code;
    this.httpStatusCode = httpStatusCode;
  }

  public String getCode() {
    return code;
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }
}

