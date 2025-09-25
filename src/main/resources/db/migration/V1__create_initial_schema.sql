-- Criação do schema inicial completo da barbearia

-- Tabela de Roles
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

-- Tabela de Usuários
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de relacionamento usuário-roles (muitos para muitos)
CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, role_id),
    CONSTRAINT fk_usuario_roles_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_roles_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

-- Tabela de Clientes
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    usuario_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Tabela de Barbeiros
CREATE TABLE barbeiro (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidades VARCHAR(255),
    telefone VARCHAR(20),
    ativo BOOLEAN DEFAULT true,
    usuario_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_barbeiro_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Tabela de Serviços
CREATE TABLE servicos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    preco DECIMAL(10,2) NOT NULL,
    duracao_minutos INTEGER NOT NULL,
    ativo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Agendamentos
CREATE TABLE agendamentos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    barbeiro_id BIGINT NOT NULL,
    servico_id BIGINT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'AGENDADO',
    observacoes VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_agendamentos_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE,
    CONSTRAINT fk_agendamentos_barbeiro FOREIGN KEY (barbeiro_id) REFERENCES barbeiro(id) ON DELETE CASCADE,
    CONSTRAINT fk_agendamentos_servico FOREIGN KEY (servico_id) REFERENCES servicos(id) ON DELETE CASCADE
);

-- Tabela de Configuração da Barbearia
CREATE TABLE configuracao_barbearia (
    id BIGSERIAL PRIMARY KEY,
    hora_abertura TIME NOT NULL,
    hora_fechamento TIME NOT NULL,
    intervalo_agendamento_minutos INTEGER DEFAULT 30,
    antecedencia_minima_horas INTEGER DEFAULT 2,
    ativo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela para armazenar os dias da semana que a barbearia funciona
CREATE TABLE dias_funcionamento (
    configuracao_id BIGINT NOT NULL,
    dia_semana INTEGER NOT NULL,
    CONSTRAINT fk_dias_funcionamento_config FOREIGN KEY (configuracao_id) REFERENCES configuracao_barbearia(id) ON DELETE CASCADE,
    CONSTRAINT chk_dia_semana CHECK (dia_semana >= 1 AND dia_semana <= 7)
);

-- Tabela de Horários de Trabalho dos Barbeiros
CREATE TABLE horario_trabalho_barbeiro (
    id BIGSERIAL PRIMARY KEY,
    barbeiro_id BIGINT NOT NULL,
    dia_semana INTEGER NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    pausa_inicio TIME,
    pausa_fim TIME,
    ativo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_horario_barbeiro FOREIGN KEY (barbeiro_id) REFERENCES barbeiro(id) ON DELETE CASCADE,
    CONSTRAINT chk_horario_dia_semana CHECK (dia_semana >= 1 AND dia_semana <= 7),
    CONSTRAINT chk_horario_inicio_fim CHECK (hora_inicio < hora_fim),
    CONSTRAINT chk_pausa_inicio_fim CHECK (pausa_inicio IS NULL OR pausa_fim IS NULL OR pausa_inicio < pausa_fim)
);

-- Tabela de Exceções de Horário (Folgas, Férias, Horários Especiais)
CREATE TABLE excecao_horario_barbeiro (
    id BIGSERIAL PRIMARY KEY,
    barbeiro_id BIGINT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    hora_inicio TIME,
    hora_fim TIME,
    tipo VARCHAR(20) NOT NULL,
    observacao VARCHAR(255),
    ativo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_excecao_barbeiro FOREIGN KEY (barbeiro_id) REFERENCES barbeiro(id) ON DELETE CASCADE,
    CONSTRAINT chk_excecao_data CHECK (data_inicio <= data_fim),
    CONSTRAINT chk_excecao_hora CHECK (hora_inicio IS NULL OR hora_fim IS NULL OR hora_inicio < hora_fim),
    CONSTRAINT chk_excecao_tipo CHECK (tipo IN ('FOLGA', 'FERIAS', 'HORARIO_ESPECIAL', 'DISPONIVEL'))
);

-- Índices para otimização de consultas
CREATE INDEX idx_usuario_username ON usuario(username);
CREATE INDEX idx_cliente_usuario_id ON cliente(usuario_id);
CREATE INDEX idx_cliente_nome ON cliente(nome);
CREATE INDEX idx_barbeiro_usuario_id ON barbeiro(usuario_id);
CREATE INDEX idx_barbeiro_nome ON barbeiro(nome);
CREATE INDEX idx_barbeiro_ativo ON barbeiro(ativo);
CREATE INDEX idx_servicos_nome ON servicos(nome);
CREATE INDEX idx_servicos_ativo ON servicos(ativo);
CREATE INDEX idx_agendamentos_cliente_id ON agendamentos(cliente_id);
CREATE INDEX idx_agendamentos_barbeiro_id ON agendamentos(barbeiro_id);
CREATE INDEX idx_agendamentos_servico_id ON agendamentos(servico_id);
CREATE INDEX idx_agendamentos_data_hora ON agendamentos(data_hora);
CREATE INDEX idx_agendamentos_status ON agendamentos(status);
CREATE INDEX idx_dias_funcionamento_config ON dias_funcionamento(configuracao_id);
CREATE INDEX idx_horario_barbeiro_id ON horario_trabalho_barbeiro(barbeiro_id);
CREATE INDEX idx_horario_dia_semana ON horario_trabalho_barbeiro(barbeiro_id, dia_semana);
CREATE INDEX idx_horario_ativo ON horario_trabalho_barbeiro(ativo);
CREATE INDEX idx_excecao_barbeiro_id ON excecao_horario_barbeiro(barbeiro_id);
CREATE INDEX idx_excecao_datas ON excecao_horario_barbeiro(barbeiro_id, data_inicio, data_fim);
CREATE INDEX idx_excecao_tipo ON excecao_horario_barbeiro(tipo);
CREATE INDEX idx_excecao_ativo ON excecao_horario_barbeiro(ativo);
CREATE INDEX idx_configuracao_ativo ON configuracao_barbearia(ativo);