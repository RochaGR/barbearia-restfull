create table barbeiros (
        id BIGSERIAL PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        especialidades varchar(100),
        ativo BOOLEAN DEFAULT true,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);