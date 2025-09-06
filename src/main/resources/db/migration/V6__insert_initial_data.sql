-- Usuário admin padrão
INSERT INTO usuarios (email, senha, role, ativo) VALUES
    ('admin@barbearia.com', '$2a$10$N.HzGNVoPxzVmZ7L1.hLiOhyVY3JLxQ6Qe.P8Q5PgD8Jy7Z3Nm4xC', 'ADMIN', true);
-- Senha: admin123

-- Serviços básicos
INSERT INTO servicos (nome, descricao, preco, duracao_minutos, ativo) VALUES
                                                                          ('Corte Tradicional', 'Corte de cabelo masculino tradicional', 25.00, 30, true),
                                                                          ('Barba', 'Aparar e modelar barba', 15.00, 20, true),
                                                                          ('Corte + Barba', 'Pacote completo: corte de cabelo e barba', 35.00, 45, true),
                                                                          ('Sobrancelha', 'Design e limpeza de sobrancelha masculina', 10.00, 15, true),
                                                                          ('Lavagem + Corte', 'Lavagem, corte e finalização', 30.00, 40, true);