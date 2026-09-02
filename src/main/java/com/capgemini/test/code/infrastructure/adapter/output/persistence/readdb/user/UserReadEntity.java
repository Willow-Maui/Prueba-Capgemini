package com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * UserReadEntity - Entidad JPA para ReadDB (MySQL - nueva)
 * Réplica desnormalizada (optimizada para lectura)
 * Se sincroniza desde WriteDB (PostgreSQL) a través de Kafka
 */
@Entity
@Table(name = "users_read", indexes = {
    @Index(name = "idx_users_read_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReadEntity {

    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "dni", nullable = false, unique = true, length = 15)
    private String dni;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;

    @PrePersist
    protected void onCreate() {
        if (lastSyncAt == null) {
            lastSyncAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastSyncAt = LocalDateTime.now();
    }
}


