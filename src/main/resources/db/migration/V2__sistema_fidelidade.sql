CREATE TABLE configuracao_fidelidade (
  id BIGSERIAL PRIMARY KEY,
  cortes_para_recompensa INTEGER NOT NULL DEFAULT 5,
  percentual_desconto DECIMAL(5,2) NOT NULL DEFAULT 40.00,
  validade_cupom_dias INTEGER NOT NULL DEFAULT 30,
  ativo BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fidelidade_cliente (
  id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT NOT NULL UNIQUE,
  cortes_realizados INTEGER NOT NULL DEFAULT 0,
  total_cupons_gerados INTEGER NOT NULL DEFAULT 0,
  total_economizado DECIMAL(10,2) DEFAULT 0,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_fidelidade_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
);

CREATE TABLE cupom_desconto (
  id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT NOT NULL,
  codigo VARCHAR(20) NOT NULL UNIQUE,
  percentual_desconto DECIMAL(5,2) NOT NULL,
  status VARCHAR(20) DEFAULT 'ATIVO',
  data_expiracao DATE NOT NULL,
  agendamento_uso_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_cupom_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
  CONSTRAINT fk_cupom_agendamento_uso FOREIGN KEY (agendamento_uso_id) REFERENCES agendamentos(id),
  CONSTRAINT chk_cupom_status CHECK (status IN ('ATIVO', 'USADO', 'EXPIRADO'))
);

CREATE INDEX idx_fidelidade_cliente_cliente_id ON fidelidade_cliente(cliente_id);
CREATE INDEX idx_cupom_cliente_id_status ON cupom_desconto(cliente_id, status);

