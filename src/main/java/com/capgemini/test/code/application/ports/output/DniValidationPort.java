package com.capgemini.test.code.application.ports.output;

/**
 * Puerto de salida (interfaz) para validación de DNI contra servicio externo.
 * Define el contrato que debe cumplir cualquier adaptador de validación de DNI.
 *
 * Implementación concreta: DniValidationAdapter (usa Feign client)
 *
 * La API externa (MockServer):
 * - PATCH http://localhost:1080/check-dni
 * - Body: { "dni": "..." }
 * - Response: HTTP 200 (válido) o HTTP 409 (inválido)
 */
public interface DniValidationPort {

  /**
   * Valida un DNI contra el servicio externo.
   *
   * @param dni el DNI a validar (String)
   * @throws com.capgemini.test.code.domain.exceptions.InvalidDniException
   *         si el DNI es rechazado por el servicio externo (HTTP 409)
   */
  void validate(String dni);
}

