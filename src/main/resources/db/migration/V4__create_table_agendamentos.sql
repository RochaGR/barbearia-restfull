CREATE TABLE agendamentos (
        id BIGSERIAL PRIMARY KEY,
        cliente_id BIGINT NOT NULL REFERENCES clientes(id),
        barbeiro_id BIGINT NOT NULL REFERENCES barbeiros(id),
        servico_id BIGINT NOT NULL REFERENCES servicos(id),
        data_hora TIMESTAMP NOT NULL,
        status VARCHAR(20) DEFAULT 'AGENDADO',
        observacoes TEXT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
