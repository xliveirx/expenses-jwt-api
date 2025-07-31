# 💸 Expenses JWT API

API RESTful desenvolvida em **Java + Spring Boot**, com autenticação baseada em **JWT (JSON Web Tokens)**, para controle de despesas pessoais. Esta aplicação permite o cadastro de usuários, login e gerenciamento de despesas de forma segura e organizada.

## 🚀 Funcionalidades

- 🔐 Autenticação e autorização com JWT
- 👤 Cadastro e login de usuários
- 📊 Cadastro, listagem, atualização e remoção de despesas
- 📄 Documentação automática com Swagger/OpenAPI
- 🧪 Testes automatizados com Spring Boot Test e Spring Security Test
- 🐳 Contêineres com Docker + docker-compose

## 🛠️ Tecnologias utilizadas

| Categoria           | Tecnologia/Biblioteca                           |
|---------------------|-------------------------------------------------|
| Linguagem           | Java 17                                         |
| Framework           | Spring Boot 3.5.0                               |
| Autenticação        | Spring Security + [Auth0 Java JWT](https://github.com/auth0/java-jwt) |
| Documentação        | SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`) |
| Persistência        | Spring Data JPA                                 |
| Banco de dados      | PostgreSQL                                      |
| ORM                 | Hibernate (via JPA)                             |
| Gerenciador de deps | Maven                                           |
| Conveniência        | Lombok                                          |
| DevOps              | Docker, docker-compose                          |


## 📦 Instalação e Execução

### Com Docker

```bash
docker-compose up --build
