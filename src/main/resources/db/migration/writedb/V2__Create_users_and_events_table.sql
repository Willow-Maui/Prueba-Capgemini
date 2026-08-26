-- WriteDB: Tabla de usuarios (maestro)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(6) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    dni VARCHAR(9) NOT NULL UNIQUE,
    phone VARCHAR(9) NOT NULL,
    role VARCHAR(20) NOT NULL,
    room_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE RESTRICT,
    INDEX idx_users_email (email),
    INDEX idx_users_room_id (room_id)
);

-- WriteDB: Event Store (auditoría y sincronización)
CREATE TABLE IF NOT EXISTS events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    aggregate_id BIGINT NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    payload JSON NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    published TINYINT(1) DEFAULT 0,
    INDEX idx_events_aggregate (aggregate_type, aggregate_id),
    INDEX idx_events_published (published),
    INDEX idx_events_created_at (created_at)
);

