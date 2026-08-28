-- ReadDB: Tabla de salas (réplica de lectura)
CREATE TABLE IF NOT EXISTS rooms_read (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_sync_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO rooms (id, name) VALUES (1, 'Sala 1'),(2,'Sala 2');
