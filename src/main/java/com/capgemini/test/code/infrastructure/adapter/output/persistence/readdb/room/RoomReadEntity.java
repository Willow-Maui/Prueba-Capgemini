package com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * RoomReadEntity - Entidad JPA para ReadDB (MySQL - nueva)
 * Réplica desnormalizada (optimizada para lectura)
 */
@Entity
@Table(name = "rooms_read")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomReadEntity {

    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;

    @PreUpdate
    protected void onUpdate() {
        lastSyncAt = LocalDateTime.now();
    }
}


