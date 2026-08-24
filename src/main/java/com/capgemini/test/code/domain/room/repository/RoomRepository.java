package com.capgemini.test.code.domain.room.repository;

import com.capgemini.test.code.domain.room.model.Room;

/**
 * Puerto de salida (interfaz) para persistencia de Sala.
 * Define el contrato que debe cumplir cualquier adaptador de persistencia.
 *
 * Implementación concreta: RoomPersistenceAdapter (usa JPA/Spring Data)
 */
public interface RoomRepository {

  /**
   * Busca una sala por su ID.
   * @param roomId el ID de la sala
   * @return la sala si existe
   *         si la sala no existe
   */
  Room findById(Long roomId);
}

