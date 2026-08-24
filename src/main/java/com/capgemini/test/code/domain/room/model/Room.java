package com.capgemini.test.code.domain.room.model;

import java.util.Objects;

/**
 * Entidad de dominio que representa una Sala.
 *
 * Una sala contiene N usuarios. Cada usuario pertenece a una única sala.
 *
 * Immutable.
 */
public class Room {

  private final Long id;
  private final String name;

  public Room(Long id, String name) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Room id must be positive");
    }
    this.id = id;
    this.name = Objects.requireNonNull(name, "Room name cannot be null");
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Room)) return false;
    Room room = (Room) o;
    return Objects.equals(id, room.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Room{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}

