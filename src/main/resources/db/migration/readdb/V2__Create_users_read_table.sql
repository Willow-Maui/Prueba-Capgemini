-- ReadDB: Tabla de usuarios (réplica de lectura desnormalizada)
CREATE TABLE IF NOT EXISTS users_read (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    dni VARCHAR(15) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    role VARCHAR(50) NOT NULL,
    room_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_sync_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms_read(id),
    INDEX idx_users_read_email (email),
    INDEX idx_users_read_room_id (room_id)
);


INSERT INTO users_read (name, email  role, room_id) VALUES
                                                        ('Juan Pérez', 'juan@example.com','12345678A',13,'600000001','ADMIN', 1),
                                                        ('María López', 'maria@example.com','12345678B','600000002', 'USER', 2);