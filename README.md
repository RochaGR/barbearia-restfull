# 💈 Sistema de Gerenciamento de Barbearia

Um sistema completo para gerenciamento de barbearias desenvolvido em Spring Boot, oferecendo controle total sobre agendamentos, horários de trabalho, clientes, barbeiros e serviços.

## 🚀 Funcionalidades

### 👥 Gestão de Usuários
- **Autenticação JWT** com roles diferenciadas
- **Cadastro de Clientes** - Registro público para novos clientes
- **Cadastro de Barbeiros** - Gerenciamento de profissionais
- **Administração** - Controle total do sistema

### 📅 Sistema de Agendamentos
- **Marcação de Horários** - Interface intuitiva para agendamentos
- **Validação Inteligente** - Verificação automática de disponibilidade
- **Status de Agendamento** - Controle do ciclo de vida (Agendado → Confirmado → Concluído)
- **Histórico Completo** - Consulta de agendamentos por cliente ou barbeiro

### ⏰ Controle de Horários
- **Horários de Trabalho** - Configuração flexível por barbeiro e dia da semana
- **Exceções de Horário** - Gestão de folgas, férias e horários especiais
- **Disponibilidade em Tempo Real** - Consulta de horários livres
- **Intervalos Personalizados** - Controle de pausas e intervalos

### 🎯 Gestão de Serviços
- **Catálogo de Serviços** - Cadastro completo com preços e duração
- **Controle de Status** - Ativação/desativação de serviços
- **Especialidades** - Vinculação de serviços com barbeiros

### ⚙️ Configurações da Barbearia
- **Horário de Funcionamento** - Definição de abertura e fechamento
- **Dias de Funcionamento** - Configuração da semana de trabalho
- **Intervalos de Agendamento** - Personalização dos slots de tempo
- **Antecedência Mínima** - Controle do prazo mínimo para agendamentos

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados principal
- **Flyway** - Controle de migrações
- **JWT** - Autenticação stateless
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

## 📋 Pré-requisitos

- Java 17+
- PostgreSQL 12+
- Maven 3.6+

## 🚀 Instalação e Execução

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/barbearia-rest.git
cd barbearia-rest
```

### 2. Configure o banco de dados
```sql
-- No PostgreSQL, execute:
CREATE DATABASE barbearia_db;
CREATE USER postgres WITH PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE barbearia_db TO postgres;
```

### 3. Configure as propriedades
Ajuste o arquivo `src/main/resources/application.properties` se necessário:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/barbearia_db
spring.datasource.username=postgres
spring.datasource.password=1234
```

### 4. Execute as migrações
```bash
mvn flyway:migrate
```

### 5. Execute a aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## 🔐 Autenticação

### Login Padrão (Admin)
```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### Resposta
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Usando o Token
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 📚 API Endpoints

### 🔑 Autenticação
- `POST /auth/login` - Login de usuários
- `POST /auth/registro-cliente` - Cadastro de novos clientes
- `POST /auth/registro-barbeiro` - Cadastro de novos barbeiros (admin)

### 👥 Usuários
- `GET /usuarios` - Listar usuários (admin)
- `POST /usuarios` - Criar usuário (admin)

### 👤 Clientes
- `GET /clientes/todos` - Listar clientes
- `GET /clientes/{id}` - Buscar cliente por ID
- `PUT /clientes/{id}` - Atualizar cliente
- `DELETE /clientes/{id}` - Excluir cliente (admin)

### 💈 Barbeiros
- `GET /barbeiros/todos` - Listar barbeiros
- `GET /barbeiros/{id}` - Buscar barbeiro por ID
- `PUT /barbeiros/{id}` - Atualizar barbeiro
- `DELETE /barbeiros/{id}` - Excluir barbeiro (admin)

### ✂️ Serviços
- `GET /servicos` - Listar todos os serviços
- `GET /servicos/ativos` - Listar serviços ativos
- `GET /servicos/{id}` - Buscar serviço por ID
- `POST /servicos` - Criar serviço (barbeiro/admin)
- `PUT /servicos/{id}` - Atualizar serviço (barbeiro/admin)
- `PATCH /servicos/{id}/status` - Ativar/desativar serviço
- `DELETE /servicos/{id}` - Excluir serviço (admin)

### 📅 Agendamentos
- `GET /agendamentos` - Listar agendamentos (admin/barbeiro)
- `GET /agendamentos/{id}` - Buscar agendamento por ID
- `GET /agendamentos/cliente/{id}` - Agendamentos do cliente
- `GET /agendamentos/barbeiro/{id}` - Agendamentos do barbeiro
- `POST /agendamentos` - Criar agendamento (cliente/admin)
- `PUT /agendamentos/{id}` - Atualizar agendamento
- `PATCH /agendamentos/{id}/status` - Alterar status
- `DELETE /agendamentos/{id}` - Excluir agendamento (admin)

### ⏰ Horários dos Barbeiros
- `GET /barbeiros/{id}/horarios` - Horários de trabalho
- `POST /barbeiros/{id}/horarios` - Definir horário (admin)
- `PUT /barbeiros/{id}/horarios/{horarioId}` - Atualizar horário (admin)
- `DELETE /barbeiros/{id}/horarios/{horarioId}` - Remover horário (admin)

### 🚫 Exceções de Horário
- `GET /barbeiros/{id}/horarios/excecoes` - Listar exceções
- `POST /barbeiros/{id}/horarios/excecoes` - Criar exceção (admin)
- `PUT /barbeiros/{id}/horarios/excecoes/{id}` - Atualizar exceção (admin)
- `DELETE /barbeiros/{id}/horarios/excecoes/{id}` - Remover exceção (admin)

### 🔍 Disponibilidade
- `GET /barbeiros/{id}/horarios/disponiveis?data=2024-01-15` - Horários disponíveis em uma data
- `GET /barbeiros/{id}/horarios/disponiveis/periodo?dataInicio=2024-01-15&dataFim=2024-01-20` - Disponibilidade em período

### ⚙️ Configurações
- `GET /admin/configuracao/barbearia` - Obter configurações (admin)
- `POST /admin/configuracao/barbearia` - Criar configurações (admin)
- `PUT /admin/configuracao/barbearia/{id}` - Atualizar configurações (admin)

## 👥 Roles e Permissões

### 🔴 ADMIN
- Acesso total ao sistema
- Gerenciamento de usuários, barbeiros e clientes
- Configuração de horários e exceções
- Controle de serviços

### 💈 BARBEIRO
- Visualização de próprios agendamentos
- Gerenciamento de serviços
- Atualização de status de agendamentos
- Consulta de clientes

### 👤 CLIENTE
- Criação e visualização de próprios agendamentos
- Consulta de barbeiros e serviços disponíveis
- Atualização de dados pessoais

## 📊 Estrutura do Banco

O sistema utiliza as seguintes entidades principais:
- **Usuario** - Dados de autenticação
- **Role** - Perfis de acesso
- **Cliente** - Informações dos clientes
- **Barbeiro** - Dados dos profissionais
- **Servico** - Catálogo de serviços
- **Agendamento** - Marcações de horários
- **HorarioTrabalhoBarbeiro** - Horários de trabalho
- **ExcecaoHorarioBarbeiro** - Folgas e exceções
- **ConfiguracaoBarbearia** - Configurações gerais

## 🔧 Configurações Avançadas

### JWT
```properties
jwt.secret=sua-chave-secreta-super-segura
jwt.expiration=3600000
```

### Flyway
```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

### Timezone
```properties
spring.jackson.time-zone=America/Sao_Paulo
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
```

## 📝 Exemplo de Uso

📝 Exemplo de Uso Completo
Fluxo Típico de Uso:
1. Login como Admin (configuração inicial)
bashcurl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
2. Cadastrar um Barbeiro (como admin)
bashcurl -X POST http://localhost:8080/auth/registro-barbeiro \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN" \
  -d '{
    "username": "barbeiro@email.com",
    "password": "123456",
    "nome": "Carlos Silva",
    "especialidades": "Corte masculino, Barba",
    "telefone": "(11) 98888-8888",
    "ativo": true
  }'
3. Criar um Serviço (como admin/barbeiro)
bashcurl -X POST http://localhost:8080/servicos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN" \
  -d '{
    "nome": "Corte Masculino",
    "descricao": "Corte tradicional masculino",
    "preco": 25.00,
    "duracaoMinutos": 30,
    "ativo": true
  }'
4. Configurar Horário do Barbeiro (como admin)
bashcurl -X POST http://localhost:8080/barbeiros/1/horarios \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN" \
  -d '{
    "diaSemana": 1,
    "horaInicio": "08:00",
    "horaFim": "18:00",
    "pausaInicio": "12:00",
    "pausaFim": "13:00"
  }'
5. Cadastrar um Cliente
bashcurl -X POST http://localhost:8080/auth/registro-cliente \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao@email.com",
    "password": "123456",
    "nome": "João Silva",
    "telefone": "(11) 99999-9999"
  }'
6. Login como Cliente
bashcurl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao@email.com",
    "password": "123456"
  }'
7. Consultar Horários Disponíveis
bashcurl -X GET "http://localhost:8080/barbeiros/1/horarios/disponiveis?data=2024-09-25" \
  -H "Authorization: Bearer TOKEN_CLIENTE"
8. Criar um Agendamento
bashcurl -X POST http://localhost:8080/agendamentos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_CLIENTE" \
  -d '{
    "clienteId": 1,
    "barbeiroId": 1,
    "servicoId": 1,
    "dataHora": "2024-09-25T14:30:00",
    "observacoes": "Corte baixo nas laterais"
  }'

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request



## 📞 Contato

Gustavo Garcia Rocha - gutop.rocha@gmail.com

Link do Projeto: https://github.com/RochaGR/barbearia-restfull

---

⭐ Se este projeto te ajudou, deixe uma estrela no GitHub!
