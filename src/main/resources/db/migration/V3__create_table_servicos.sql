CREATE TABLE servicos (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          descricao VARCHAR(500),
                          preco DECIMAL(10,2) NOT NULL,
                          duracao_minutos INTEGER NOT NULL,
                          ativo BOOLEAN DEFAULT true,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_servicos_nome ON servicos(nome);
CREATE INDEX idx_servicos_ativo ON servicos(ativo);