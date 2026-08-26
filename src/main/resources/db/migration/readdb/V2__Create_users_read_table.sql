-- ReadDB: Tabla de usuarios (réplica de lectura desnormalizada)
CREATE TABLE IF NOT EXISTS users_read (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(6) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    dni VARCHAR(9) NOT NULL UNIQUE,
    phone VARCHAR(9) NOT NULL,
    role VARCHAR(20) NOT NULL,
    room_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_sync_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms_read(id),
    INDEX idx_users_read_email (email),
    INDEX idx_users_read_room_id (room_id)
);

