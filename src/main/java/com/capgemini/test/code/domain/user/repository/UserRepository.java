package com.capgemini.test.code.domain.user.repository;

import com.capgemini.test.code.domain.user.model.User;

/**
 * Puerto de salida (interfaz) para persistencia de Usuario.
 * Define el contrato que debe cumplir cualquier adaptador de persistencia.
 *
 * Implementación concreta: UserPersistenceAdapter (usa JPA/Spring Data)
 */
public interface UserRepository {

  /**
   * Busca un usuario por su ID.
   * @param id el ID del usuario
   * @return el usuario si existe
   */
  User findById(Long id);

  /**
   * Busca un usuario por su email.
   * @param email el email del usuario
   * @return el usuario si existe
   */
  User findByEmail(String email);

  /**
   * Verifica si un email ya existe en el sistema.
   * @param email el email a verificar
   * @return true si el email ya existe, false en caso contrario
   */
  boolean existsByEmail(String email);

  /**
   * Verifica si un DNI ya existe en el sistema.
   * @param dni el DNI a verificar
   * @return true si el DNI ya existe, false en caso contrario
   */
  boolean existsByDni(String dni);

  /**
   * Guarda un usuario en la persistencia.
   * Si el usuario no tiene ID, se genera uno automáticamente.
   *
   * @param user el usuario a guardar (sin ID)
   * @return el usuario guardado (con ID asignado por la BD)
   */
  User save(User user);

  /**
   * Obtiene un usuario por su ID dentro de una sala específica.
   * @param userId el ID del usuario
   * @param roomId el ID de la sala
   * @return el usuario si existe en la sala
   */
  User findByIdAndRoomId(Long userId, Long roomId);
}

