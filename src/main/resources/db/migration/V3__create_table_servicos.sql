CREATE TABLE servicos (
        id BIGSERIAL PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        preco DECIMAL(10,2) NOT NULL,
        duracao_minutos INTEGER NOT NULL,
        ativo BOOLEAN DEFAULT true
);