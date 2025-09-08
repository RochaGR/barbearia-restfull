CREATE TABLE barbeiros (
                           id BIGSERIAL PRIMARY KEY,
                           nome VARCHAR(100) NOT NULL,
                           especialidades VARCHAR(255),
                           ativo BOOLEAN DEFAULT true,
                           usuario_id BIGINT NOT NULL UNIQUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_barbeiros_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_barbeiros_usuario_id ON barbeiros(usuario_id);
CREATE INDEX idx_barbeiros_nome ON barbeiros(nome);