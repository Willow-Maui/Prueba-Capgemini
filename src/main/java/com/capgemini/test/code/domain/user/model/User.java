package com.capgemini.test.code.domain.user.model;

import com.capgemini.test.code.domain.user.exceptions.InvalidEmailException;
import com.capgemini.test.code.domain.user.exceptions.InvalidDniException;
import com.capgemini.test.code.domain.user.exceptions.InvalidPhoneException;
import com.capgemini.test.code.domain.user.exceptions.InvalidRoleException;
import com.capgemini.test.code.domain.user.exceptions.InvalidUserNameException;

import java.util.Objects;

/**
 * Entidad de dominio que representa un Usuario.
 *
 * El usuario pertenece a una sala y tiene:
 * - id: Long (asignado por BD)
 * - name: String (1-6 caracteres)
 * - email: String (único en el sistema, contiene @ y .)
 * - dni: String (no vacío, validado por API externa)
 * - phone: String (obligatorio para SUPERADMIN, opcional para ADMIN)
 * - role: UserRole (ADMIN o SUPERADMIN, determina canal de notificación)
 * - roomId: Long (ID de la sala)
 *
 * Immutable. Use Builder para crear instancias.
 */
public class User {

  private final Long id;
  private final String name;
  private final String email;
  private final String dni;
  private final String phone;
  private final UserRole role;
  private final Long roomId;

  private User(Long id, String name, String email, String dni,
               String phone, UserRole role, Long roomId) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.dni = dni;
    this.phone = phone;
    this.role = role;
    this.roomId = roomId;
  }

  /**
   * Factory method para crear un User usando Builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  // Getters
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getDni() {
    return dni;
  }

  public String getPhone() {
    return phone;
  }

  public UserRole getRole() {
    return role;
  }

  public Long getRoomId() {
    return roomId;
  }

  /**
   * Obtiene el canal de notificación según el rol.
   */
  public UserRole.NotificationChannel getNotificationChannel() {
    return role.getNotificationChannel();
  }

  /**
   * La identidad de un usuario es su email (es único en el sistema).
   * Dos usuarios con el mismo email se consideran el mismo usuario.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User user = (User) o;
    return Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", role=" + role +
        ", roomId=" + roomId +
        '}';
  }

  /**
   * Builder para crear instancias de User de forma segura y validada.
   */
  public static class Builder {

    private String name;
    private String email;
    private String dni;
    private String phone;
    private String role;
    private Long roomId;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder dni(String dni) {
      this.dni = dni;
      return this;
    }

    public Builder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder role(String role) {
      this.role = role;
      return this;
    }

    public Builder roomId(Long roomId) {
      this.roomId = roomId;
      return this;
    }

    /**
     * Construye y valida el User.
     *
     * Validaciones aplicadas:
     * - Nombre: no vacío, máximo 6 caracteres
     * - Email: no vacío, contiene @ y .
     * - DNI: no vacío
     * - Rol: no vacío, ADMIN o SUPERADMIN
     * - Phone: obligatorio para SUPERADMIN, opcional para ADMIN
     * - RoomId: no nulo y positivo
     *
     * @return User instancia validada
     */
    public User build() {
      // Validar nombre
      if (name == null || name.isBlank()) {
        throw new InvalidUserNameException("Name cannot be empty");
      }
      if (name.length() > 6) {
        throw new InvalidUserNameException("Name cannot exceed 6 characters");
      }

      // Validar email
      if (email == null || email.isBlank()) {
        throw new InvalidEmailException("Email cannot be empty");
      }
      if (!email.contains("@")) {
        throw new InvalidEmailException("Email must contain @");
      }
      if (!email.contains(".")) {
        throw new InvalidEmailException("Email must contain .");
      }

      // Validar DNI
      if (dni == null || dni.isBlank()) {
        throw new InvalidDniException("DNI cannot be empty");
      }

      // Validar role
      if (role == null || role.isBlank()) {
        throw new InvalidRoleException("Role cannot be empty");
      }
      UserRole userRole;
      try {
        userRole = UserRole.valueOf(role.toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new InvalidRoleException(role);
      }

      // Validar phone (obligatorio para SUPERADMIN)
      if (userRole == UserRole.SUPERADMIN) {
        if (phone == null || phone.isBlank()) {
          throw new InvalidPhoneException("Phone is required for SUPERADMIN");
        }
      }

      // Validar roomId
      if (roomId == null || roomId <= 0) {
        throw new IllegalArgumentException("RoomId must be positive");
      }

      // Crear y retornar User (sin ID, lo asignará la BD)
      return new User(null, name.trim(), email.trim(), dni.trim(),
                      phone != null ? phone.trim() : null, userRole, roomId);
    }

    /**
     * Construye un User con un ID específico (usado principalmente en lectura desde BD).
     */
    public User buildWithId(Long userId) {
      User user = build();
      return new User(userId, user.name, user.email, user.dni, user.phone, user.role, user.roomId);
    }
  }
}

