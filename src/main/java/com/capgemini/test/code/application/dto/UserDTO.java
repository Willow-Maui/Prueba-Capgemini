package com.capgemini.test.code.application.dto;

/**
 * DTO para lectura de Usuario.
 * Usado en GET /users/{id}
 */
public class UserDTO {

  private Long id;
  private String name;
  private String email;
  private String phone;
  private String dni;
  private String rol;
  private Long roomId;

  // Constructores
  public UserDTO() {
  }

  public UserDTO(Long id, String name, String email, String phone, String dni, String rol, Long roomId) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.dni = dni;
    this.rol = rol;
    this.roomId = roomId;
  }

  // Getters y Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public Long getRoomId() {
    return roomId;
  }

  public void setRoomId(Long roomId) {
    this.roomId = roomId;
  }

  @Override
  public String toString() {
    return "UserDTO{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", rol='" + rol + '\'' +
        ", roomId=" + roomId +
        '}';
  }
}

