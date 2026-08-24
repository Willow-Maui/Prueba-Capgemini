package com.capgemini.test.code.domain.room.exceptions;

import com.capgemini.test.code.domain.exceptions.DomainException;

/**
 * Excepción lanzada cuando una sala no es encontrada en la persistencia.
 * HTTP 404 Not Found.
 */
public class RoomNotFoundException extends DomainException {
  public RoomNotFoundException(Long roomId) {
    super("Room not found with ID: " + roomId, "ROOM_NOT_FOUND", 404);
  }
}

