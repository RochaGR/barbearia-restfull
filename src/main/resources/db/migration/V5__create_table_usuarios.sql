CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          senha VARCHAR(255) NOT NULL,
                          role VARCHAR(50) NOT NULL CHECK (role IN ('CLIENTE', 'BARBEIRO', 'ADMIN')),
                          ativo BOOLEAN NOT NULL DEFAULT true,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_role ON usuarios(role);