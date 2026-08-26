-- WriteDB: Tabla de salas (maestro)
CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar sala inicial (required por aplicación)
INSERT INTO rooms (id, name, description) VALUES (1, 'Sala 1', 'Sala principal');

