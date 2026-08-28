-- WriteDB: Tabla de usuarios (maestro)

CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(6) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    dni VARCHAR(9) NOT NULL UNIQUE,
    phone VARCHAR(9) NOT NULL,
    role VARCHAR(20) NOT NULL,
    room_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_room
    FOREIGN KEY (room_id)
    REFERENCES rooms(id)
    ON DELETE RESTRICT
    );

CREATE INDEX IF NOT EXISTS idx_users_email
    ON users(email);

CREATE INDEX IF NOT EXISTS idx_users_room_id
    ON users(room_id);


-- WriteDB: Event Store (auditoría y sincronización)

CREATE TABLE IF NOT EXISTS events (
                                      id BIGSERIAL PRIMARY KEY,
                                      event_type VARCHAR(100) NOT NULL,
    aggregate_id BIGINT NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    published BOOLEAN DEFAULT FALSE
    );

CREATE INDEX IF NOT EXISTS idx_events_aggregate
    ON events(aggregate_type, aggregate_id);

CREATE INDEX IF NOT EXISTS idx_events_published
    ON events(published);

CREATE INDEX IF NOT EXISTS idx_events_created_at
    ON events(created_at);