<p align="center">
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" width="90" alt="Java Logo" />
  &nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg" width="90" alt="Spring Boot Logo" />
  &nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/angular/angular-original.svg" width="90" alt="Angular Logo" />
  &nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-original.svg" width="90" alt="PostgreSQL Logo" />
</p>

<h1 align="center">Cinema Tickets</h1>

<p align="center">
  Sistema acadêmico para gerenciamento e venda de ingressos de cinema
</p>

<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white" /></a>
  <a href="#"><img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" /></a>
  <a href="#"><img src="https://img.shields.io/badge/Angular-Standalone-DD0031?style=for-the-badge&logo=angular&logoColor=white" /></a>
  <a href="#"><img src="https://img.shields.io/badge/PostgreSQL-Database-336791?style=for-the-badge&logo=postgresql&logoColor=white" /></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Git-Conventional%20Commits-F05032?style=for-the-badge&logo=git&logoColor=white" />
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" />
</p>

---

## Descrição

O **Cinema Tickets** é um projeto acadêmico desenvolvido para a disciplina de **Engenharia de Software**, cujo objetivo é a criação de um **sistema de venda e gerenciamento de ingressos de cinema**.

A plataforma permite que a rede de cinemas realize o cadastro e o gerenciamento de suas salas, incluindo dados como capacidade, organização dos assentos, horários de funcionamento e filmes em exibição. Cada sala pode possuir uma programação própria, com sessões definidas por data e horário, possibilitando uma visualização clara e estruturada do catálogo disponível.

Os usuários do sistema podem consultar os filmes em cartaz de diferentes formas, como por cinema, localização, categoria, data ou nome do filme. A aplicação também oferece um fluxo simplificado para a compra de ingressos, tornando o processo mais rápido, acessível e organizado.

Além da venda de ingressos, o sistema pode oferecer funcionalidades complementares, como controle de presença nas sessões, emissão de ingressos digitais, envio de notificações ou lembretes aos clientes e coleta de feedback após as exibições.

### Estado atual do projeto

Atualmente, o projeto encontra-se em sua **fase inicial**, contendo apenas:

* A estrutura base do **backend** em Java com Spring Boot;
* A estrutura base do **frontend** em Angular;
* Organização do repositório, fluxo de contribuição e padrões de versionamento.
* Quadro Scrum para organização da equipe

---

## Tecnologias Utilizadas

### Backend

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL

### Frontend

* Angular (Standalone Components)
* TypeScript
* HTML5
* CSS3

### Ferramentas e práticas

* Git
* GitHub
* Conventional Commits
* Pull Requests com revisão obrigatória

---

## Instalação e Execução

### Pré-requisitos

* Java 21 ou superior
* PostgreSQL
* Node.js (LTS)
* npm
* Git

### Banco de Dados

Antes de rodar o backend, é necessário criar o banco de dados e o usuário no PostgreSQL. Conecte-se ao PostgreSQL como superusuário e execute:

```sql
CREATE USER cinema_app WITH PASSWORD 'password';
CREATE DATABASE cinema_tickets OWNER cinema_app;
GRANT ALL PRIVILEGES ON DATABASE cinema_tickets TO cinema_app;
```

### Backend
 
O projeto já possui um arquivo `application-dev.yaml` em `backend/src/main/resources/` configurado com os valores padrão. Caso tenha usado credenciais diferentes, edite o arquivo antes de prosseguir:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cinema_tickets
    username: cinema_app
    password: password

jwt:
  secret: "rjfoBSJbdo2DWCXzgWw42ug9VndCxurWHfAh71kq36o="
  expiration: 86400000
```

Acesse a pasta `backend` e rode o projeto com o perfil `dev`:

**Linux / macOS:**
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Windows:**
```bash
cd backend
./mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
```

A API estará disponível em `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
ng serve
```

A aplicação estará disponível em:

```
http://localhost:4200
```

---

## Mapeamento de Endpoints

A base da URL da API é `http://localhost:8080`. Todos os endpoints estão listados abaixo.

### Autenticação — `/auth`

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| `POST` | `/auth/register` | Cadastra um novo usuário | Não |
| `POST` | `/auth/login` | Realiza login e retorna o token JWT | Não |

**Corpo de `/auth/register`:**
```json
{
  "email": "usuario@email.com",
  "password": "senha123",
  "nome": "Nome Completo",
  "cpf": "12345678900",
  "celular": "81999999999"
}
```

**Corpo de `/auth/login`:**
```json
{
  "email": "usuario@email.com",
  "password": "senha123"
}
```

**Resposta de `/auth/login`:**
```json
{
  "accessToken": "<jwt>",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

---

### Filmes — `/filmes`

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| `GET` | `/filmes` | Lista todos os filmes | Não |
| `GET` | `/filmes/{id}` | Retorna um filme pelo ID | Não |

**Exemplo de resposta:**
```json
{
  "id": 1,
  "titulo": "Nome do Filme",
  "poster": "https://url-da-imagem.jpg",
  "backdrop": "https://url-do-backdrop.jpg",
  "classificacao": "14",
  "duracao": 120,
  "generos": ["Ação", "Aventura"],
  "diretores": ["Nome do Diretor"],
  "sinopse": "Descrição do filme...",
  "elenco": ["Ator 1", "Ator 2"],
  "status": "EM_CARTAZ"
}
```

---

### Salas — `/salas`

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| `GET` | `/salas` | Lista todas as salas | Não |
| `GET` | `/salas/{id}` | Retorna uma sala pelo ID | Não |

**Exemplo de resposta:**
```json
{
  "id": 1,
  "nome": "Sala 1",
  "capacidade": 100
}
```

---

### Sessões — `/sessoes`

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| `POST` | `/sessoes` | Cria uma nova sessão | Sim |
| `GET` | `/sessoes?data=YYYY-MM-DD` | Lista sessões por data | Não |
| `GET` | `/sessoes/{id}` | Retorna uma sessão pelo ID | Não |

**Corpo de `POST /sessoes`:**
```json
{
  "filmeId": 1,
  "salaId": 2,
  "inicio": "2026-03-01T19:30:00",
  "tipo": "2D"
}
```

**Exemplo de resposta:**
```json
{
  "id": 1,
  "filmeId": 1,
  "salaId": 2,
  "inicio": "2026-03-01T19:30:00",
  "tipo": "2D"
}
```

**Exemplo de requisição `GET /sessoes?data=2026-03-01`:**
```
GET http://localhost:8080/sessoes?data=2026-03-01
```

**Exemplo de resposta:**
```json
[
  {
    "id": 1,
    "filmeId": 1,
    "salaId": 2,
    "inicio": "2026-03-01T14:00:00",
    "tipo": "2D"
  },
  {
    "id": 2,
    "filmeId": 1,
    "salaId": 3,
    "inicio": "2026-03-01T19:30:00",
    "tipo": "3D"
  }
]
```

---

## Instruções de Uso

No profile dev, há um perfil de Admin cadastrado, com credenciais:

email: admin@local.dev
password: admin1

Use esse **login** para utilizar a funcionalidade de cadastrar Sessões.

---

## Guia de Contribuição

O projeto segue um fluxo de contribuição organizado, utilizando boas práticas de versionamento, colaboração em equipe e gerenciamento de tarefas.

### Organização da equipe e tarefas

A equipe utiliza o **GitHub Projects (Quadro Scrum)** como ferramenta de organização e acompanhamento do desenvolvimento do projeto. O fluxo adotado é o seguinte:

- As funcionalidades, correções e melhorias são registradas como **Issues** no repositório;
- Cada issue é adicionada ao **Quadro Scrum** e atribuída a um integrante da equipe;
- O progresso das tarefas é acompanhado por meio das colunas do quadro (por exemplo: *To Do*, *In Progress* e *Done*);
- As issues são resolvidas por meio de **commits** e **Pull Requests**, mantendo a rastreabilidade entre planejamento e código;
- Commits e Pull Requests podem:
  - referenciar a issue relacionada (`Related to #id`), ou
  - encerrar automaticamente a issue ao final do desenvolvimento (`Closes #id`).

### Fluxo de versionamento

- Cada integrante trabalha em um **fork** do repositório principal;
- A branch `main` é **protegida**, não permitindo commits diretos;
- Todas as alterações são realizadas por meio de **Pull Requests**;
- Cada Pull Request exige:
  - uso do padrão **Conventional Commits**;
  - no mínimo **2 revisores**;
  - resolução de todos os comentários antes do merge.

### Padrão de commit

```

feat(escopo): descrição curta

```

Exemplo:

```

feat(frontend): initialize Angular standalone application

- Criada a estrutura inicial do frontend em Angular
- Configurado o projeto no padrão standalone
- Ajustadas configurações iniciais do workspace

Related to #10

```

---

## Contribuidores

* **[Arthur Roberto Araújo Tavares](https://github.com/Arthur-789)**
* **[Hugo Matheus Costa Araújo](https://github.com/hugomtths)**
* **[Luís Henrique Domingos da Silva](https://github.com/LuisH07)**
* **[Maria Luiza Bezerra dos Santos](https://github.com/marialuizab11)**
* **[Raphael Augusto Paulino Leite](https://github.com/rapael-augusto)**

---

## 📄 Licença

Este projeto está licenciado sob a **Licença MIT**.
Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---
