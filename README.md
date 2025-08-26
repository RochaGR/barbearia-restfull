# 💈 Barbearia REST

Sistema completo para gerenciamento de barbearia, desenvolvido em **Java Spring Boot** com **PostgreSQL** e **Flyway**.  
O projeto oferece **APIs REST** para controle de clientes, barbeiros, serviços e agendamentos, incluindo **validações de negócio** e **controle de disponibilidade de horários**.

---

## 🚀 Funcionalidades

- 📌 **Gestão de Clientes**: Cadastro, consulta, atualização e exclusão
- ✂️ **Gestão de Barbeiros**: Controle completo da equipe e horários de trabalho
- 💵 **Catálogo de Serviços**: Gestão de serviços (corte, barba, hidratação, etc.) com preços
- 🗓️ **Sistema de Agendamentos**: Agendamento inteligente com validação de disponibilidade
- ✅ **Validações de Negócio**: Prevenção de conflitos de horário e regras de negócio
- 🔒 **Arquitetura Segura**: Estrutura preparada para autenticação e autorização

---

## 🛠️ Tecnologias Utilizadas

- **Java 24** - Linguagem de programação
- **Spring Boot** - Framework principal
- **Spring Data JPA** - Mapeamento objeto-relacional
- **Bean Validation** - Validação de dados
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Versionamento de banco de dados
- **Maven** - Gerenciamento de dependências

---

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 24** ou superior
- **PostgreSQL 12** ou superior
- **Maven 3.6** ou superior

---

## ⚙️ Configuração e Execução

### 1. Clone o repositório
```bash
git clone https://github.com/RochaGR/barbearia-rest.git
cd barbearia-rest
```

### 2. Configure o banco de dados
Crie um banco PostgreSQL e configure as credenciais no `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/barbearia_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Execute as migrações
```bash
mvn flyway:migrate
```

### 4. Inicie a aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

---

## 📂 Estrutura do Projeto

```
barbearia-rest/
┣ 📂 src/
┃ ┣ 📂 main/
┃ ┃ ┣ 📂 java/com/br/barbeariaRest/
┃ ┃ ┃ ┣ 📂 controller/     # Controladores REST
┃ ┃ ┃ ┣ 📂 service/       # Regras de negócio
┃ ┃ ┃ ┣ 📂 repository/    # Acesso aos dados
┃ ┃ ┃ ┣ 📂 model/         # Entidades JPA
┃ ┃ ┃ ┗ 📂 config/        # Configurações
┃ ┃ ┗ 📂 resources/
┃ ┃   ┣ 📂 db/migration/  # Scripts Flyway
┃ ┃   ┗ 📜 application.properties
┃ ┗ 📂 test/              # Testes unitários
┣ 📜 pom.xml
┗ 📜 README.md
```

---

## 🌐 Endpoints da API

### Clientes
- `GET /clientes` - Lista todos os clientes
- `POST /clientes` - Cadastra novo cliente
- `GET /clientes/{id}` - Busca cliente por ID
- `PUT /clientes/{id}` - Atualiza cliente
- `DELETE /clientes/{id}` - Remove cliente

### Barbeiros
- `GET /barbeiros` - Lista todos os barbeiros
- `POST /barbeiros` - Cadastra novo barbeiro
- `GET /barbeiros/{id}` - Busca barbeiro por ID
- `PUT /barbeiros/{id}` - Atualiza barbeiro
- `DELETE /barbeiros/{id}` - Remove barbeiro

### Serviços
- `GET /servicos` - Lista todos os serviços
- `POST /servicos` - Cadastra novo serviço
- `GET /servicos/{id}` - Busca serviço por ID
- `PUT /servicos/{id}` - Atualiza serviço
- `DELETE /servicos/{id}` - Remove serviço

### Agendamentos
- `GET /agendamentos` - Lista todos os agendamentos
- `POST /agendamentos` - Cria novo agendamento
- `GET /agendamentos/{id}` - Busca agendamento por ID
- `PUT /agendamentos/{id}` - Atualiza agendamento
- `DELETE /agendamentos/{id}` - Cancela agendamento
- `GET /agendamentos/disponibilidade` - Verifica horários disponíveis

---

## 🧪 Testes

Execute os testes com:
```bash
mvn test
```

---

## 📝 Próximas Melhorias

- [ ] Implementar autenticação JWT
- [ ] Adicionar documentação Swagger/OpenAPI
- [ ] Criar sistema de notificações
- [ ] Implementar relatórios de faturamento
- [ ] Adicionar testes de integração
- [ ] Deploy com Docker

---

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 👤 Autor

**Seu Nome**
- GitHub: [@RochaGR](https://github.com/RochaGR)
- LinkedIn: [(https://www.linkedin.com/in/gustavo-garcia-rocha-91279b300)
- Email: gutop.rocha@gmail.com

---

⭐ Se este projeto te ajudou, considere dar uma estrela!