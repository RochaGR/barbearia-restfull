CREATE TABLE agendamentos (
                              id BIGSERIAL PRIMARY KEY,
                              cliente_id BIGINT NOT NULL,
                              barbeiro_id BIGINT NOT NULL,
                              servico_id BIGINT NOT NULL,
                              data_hora TIMESTAMP NOT NULL,
                              status VARCHAR(50) DEFAULT 'AGENDADO',
                              observacoes VARCHAR(500),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_agendamentos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
                              CONSTRAINT fk_agendamentos_barbeiro FOREIGN KEY (barbeiro_id) REFERENCES barbeiros(id) ON DELETE CASCADE,
                              CONSTRAINT fk_agendamentos_servico FOREIGN KEY (servico_id) REFERENCES servicos(id) ON DELETE CASCADE
);

CREATE INDEX idx_agendamentos_cliente_id ON agendamentos(cliente_id);
CREATE INDEX idx_agendamentos_barbeiro_id ON agendamentos(barbeiro_id);
CREATE INDEX idx_agendamentos_servico_id ON agendamentos(servico_id);
CREATE INDEX idx_agendamentos_data_hora ON agendamentos(data_hora);
CREATE INDEX idx_agendamentos_status ON agendamentos(status);