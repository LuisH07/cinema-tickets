<p align="center">
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" width="90" alt="Java Logo" />
  &nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg" width="90" alt="Spring Boot Logo" />
  &nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/angular/angular-original.svg" width="90" alt="Angular Logo" />
  &nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-original.svg" width="90" alt="PostgreSQL Logo" />
  &nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original.svg" width="90" alt="Docker Logo" />
  &nbsp;&nbsp;&nbsp;
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/github/github-original.svg" width="90" alt="GitHub Logo" />
</p>

<h1 align="center">Cinema Tickets</h1>

<p align="center">
  Sistema acadêmico para gerenciamento e venda de ingressos de cinema
</p>

<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/Java-21+-ED8B00?style=for-the-badge&logo=java&logoColor=white" /></a>
  <a href="#"><img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" /></a>
  <a href="#"><img src="https://img.shields.io/badge/Angular-Standalone-DD0031?style=for-the-badge&logo=angular&logoColor=white" /></a>
  <a href="#"><img src="https://img.shields.io/badge/PostgreSQL-Database-336791?style=for-the-badge&logo=postgresql&logoColor=white" /></a>
  <a href="#"><img src="https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker&logoColor=white" /></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Git-Conventional%20Commits-F05032?style=for-the-badge&logo=git&logoColor=white" />
  <img src="https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" />
  <img src="https://img.shields.io/badge/SonarQube-Code%20Quality-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white" />
  <img src="https://img.shields.io/badge/JaCoCo-Test%20Coverage-800000?style=for-the-badge" />
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" />
</p>

---

## Descrição

O **Cinema Tickets** é um projeto acadêmico desenvolvido para a disciplina de **Engenharia de Software**, cujo objetivo é a criação de um **sistema de venda e gerenciamento de ingressos de cinema**.

A plataforma permite que a rede de cinemas realize o cadastro e o gerenciamento de suas salas, incluindo dados como capacidade, organização dos assentos, horários de funcionamento e filmes em exibição. Cada sala pode possuir uma programação própria, com sessões definidas por data e horário, possibilitando uma visualização clara e estruturada do catálogo disponível.

Os usuários do sistema podem consultar os filmes em cartaz de diferentes formas, como por cinema, localização, categoria, data ou nome do filme. A aplicação também oferece um fluxo simplificado para a compra de ingressos, tornando o processo mais rápido, acessível e organizado.

Além da venda de ingressos, o sistema pode oferecer funcionalidades complementares, como controle de presença nas sessões, emissão de ingressos digitais, envio de notificações ou lembretes aos clientes e coleta de feedback após as exibições.

### Estado atual do projeto

Atualmente, o projeto encontra-se em sua **fase intermediária**, contendo apenas:

* Cadastro de clientes e sessões no **backend**, além de autenticação de usuários
* Visualização de sessões por data, visualização das informações do filme e sessões dele, criação de sessões e 
  autenticação no **frontend**
* Organização do repositório, fluxo de contribuição e padrões de versionamento.
* **Quadro Scrum** para organização da equipe
* **SonarQubeCloud** integrado ao repositório para análise do código
* **JaCoCo** para geração de relatório de cobertura de testes
* **GitHub Actions** para CI/CD
* Aplicação online no **Render** [aqui](https://cinema-tickets-frontend-9qnw.onrender.com)

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
* GitFlow
* GitHub Actions (CI)
* SonarQube
* JaCoCo
* Docker
* Docker Compose

---

## Instalação e Execução

### Pré-requisitos

- Docker
- Docker Compose
- Git
Verifique:

```bash
docker --version
docker compose version
```

---

## Configuração do `.env`

Antes de rodar o projeto, você deve criar seu próprio arquivo `.env` na raiz do projeto.

Existe um arquivo de exemplo chamado `.env.example`. Copie ele:

```bash
cp .env.example .env
```

Depois edite o `.env` conforme necessário.

### `.env.example`

```env
# Senha do usuário do banco de dados PostgreSQL
CINEMA_TICKETS_DB_PASSWORD=sua-senha-aqui

# Segredo para JWT (JSON Web Tokens)
JWT_SECRET=sua-senha-aqui

# Coloque o perfil do spring
SPRING_PROFILES_ACTIVE=dev

# Alterar environment do angular
CONFIG=development

# Origens permitidas
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:5173

# Versão da aplicação
APP_VERSION=dev

# Tempo de expiração do JWT
JWT_EXPIRATION_MS=86400000
```

O `.env` define como a aplicação será executada (perfil dev ou prod, CORS, JWT, etc).
Verifique o que precisa ser informado em cada application para preencher corretamente o `.env` de cada perfil.
---

# Ambiente de Desenvolvimento

No ambiente de desenvolvimento utilizamos **Docker Compose**.

### Subindo a aplicação

Na raiz do projeto:

```bash
docker compose up -d --build
```

Isso irá:

- Construir as imagens
- Subir backend
- Subir frontend
- Subir PostgreSQL
- Executar seeders automaticamente

### URLs locais

Frontend:
```
http://localhost:4200
```

Backend:
```
http://localhost:8080
```

### Seeders

No profile `dev`, os seeders estão ativados. Isso significa que:

- Filmes iniciais são carregados
- Usuário admin é criado automaticamente

Admin padrão:

```
email: admin@local.dev
password: admin1
```

---

# Ambiente de Produção

Em produção a aplicação está hospedada no **Render**.

Frontend:
```
https://cinema-tickets-frontend-9qnw.onrender.com
```

Backend:
```
https://cinema-tickets-eqic.onrender.com
```

No ambiente de produção:

- O perfil Spring ativo é `prod`
- O banco é um PostgreSQL gerenciado pelo Render
- CORS permite apenas o frontend hospedado
- Seeders de desenvolvimento não são utilizados
- Bootstrap de admin pode ser ativado via variáveis de ambiente

Exemplo de variáveis utilizadas em produção:

```env
CINEMA_TICKETS_DB_URL=jdbc:postgresql://dpg-d6gdo6kr85hc7384hl90-a:5432/cinema_tickets_prod
CINEMA_TICKETS_DB_USERNAME=cinema_tickets_prod_app
CINEMA_TICKETS_DB_PASSWORD=password
SPRING_PROFILES_ACTIVE=prod
CORS_ALLOWED_ORIGINS=https://cinema-tickets-frontend-9qnw.onrender.com
APP_VERSION=prod

JWT_SECRET=...
JWT_EXPIRATION_MS=86400000

BOOTSTRAP_ADMIN_ENABLED=true
BOOTSTRAP_ADMIN_EMAIL=admin@prod.com
BOOTSTRAP_ADMIN_PASSWORD=password
BOOTSTRAP_ADMIN_NAME=Admin
BOOTSTRAP_ADMIN_CPF=00000000000
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

Após subir o projeto com Docker, acesse:

```text
http://localhost:4200
```

O frontend é o caminho recomendado para usar o sistema no dia a dia. Ele já consome a API local (`http://localhost:8080`) automaticamente.

---

### 1. Login (usuário comum ou admin)

Na tela de login, informe email e senha para entrar no sistema.

No profile `dev`, existe um usuário administrador já criado (seeder):

```text
email: admin@local.dev
password: admin1
```

Use esse login para acessar as funcionalidades de criação de sessões.

---

### 2. Cadastro de usuário (Register)

Se você ainda não tiver conta, use a opção de cadastro no frontend.

Você vai preencher os mesmos campos do endpoint `POST /auth/register`:

- Email
- Senha
- Nome
- CPF
- Celular

Após cadastrar, faça login normalmente.

### 3. Ver sessões por data

Na área de sessões, selecione uma data no seletor/calendário.

O frontend faz a busca usando:

- `GET /sessoes?data=YYYY-MM-DD`

Exemplo de comportamento esperado:
- Selecionou `2026-03-01` → o sistema lista as sessões daquele dia.

---

### 4. Criar sessão (somente Admin)

Para criar sessões, você precisa estar logado como **Admin**.

Caminho típico no frontend:
- Clicar em “Criar Sessão”
- Informar:
  - Filme (equivale a `filmeId`)
  - Sala (equivale a `salaId`)
  - Data e hora de início (equivale a `inicio` no formato ISO)
  - Tipo (ex: `2D`, `3D`)

Ao salvar, o frontend chama:

- `POST /sessoes` (com JWT automaticamente no `Authorization: Bearer <token>`)

Se você não estiver autenticado como admin (ou sem token válido), a criação será negada.

---

### 5. Visualizar informações de filme da sessão (usuário comum ou admin)

Na tela de filme, o frontend faz a busca usando:

- `GET /filmes/{id}`

Então você tem acesso a todas as informações cadastradas sobre aquele filme e as sessões disponíveis dele

---

## Guia de Contribuição

O projeto segue um fluxo de contribuição organizado, utilizando boas práticas de versionamento, colaboração em equipe e gerenciamento de tarefas.
Além disso, adota práticas modernas de integração contínua, qualidade de código e padronização de ambiente, utilizando **Docker**, **GitHub Actions**, **JaCoCo** e **SonarQube**.

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

Além disso, todo Pull Request dispara automaticamente um pipeline de **GitHub Actions**, responsável por:
- Build do projeto;
- Execução de testes automatizados;
- Geração de relatório de cobertura com **JaCoCo**;
- Análise estática de código com **SonarQube**.
- Deploy automático no **Render** caso o workflow do Actions seja bem-sucedido

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
