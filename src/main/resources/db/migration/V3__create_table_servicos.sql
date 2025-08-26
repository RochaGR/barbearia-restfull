CREATE TABLE servicos (
        id BIGSERIAL PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        descricao varchar(100) not null,
        preco DECIMAL(10,2) NOT NULL,
        duracao_minutos INTEGER NOT NULL,
        ativo BOOLEAN DEFAULT true,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);