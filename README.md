
---

### README para o Repositório do **Backend**

```markdown
# Locadora - Backend

Este repositório contém o código do backend do sistema de gerenciamento de locadora, desenvolvido com **Spring Boot**.

## Índice
- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Como Executar](#como-executar)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Contribuição](#contribuição)
- [Licença](#licença)

---

## Visão Geral

A API REST do sistema de locadora de nome Passatempo onde fornece os endpoints necessários para o gerenciamento de atores, classes, diretores, itens, títulos, clientes, locações e devoluções.

---

## Funcionalidades

- Gerenciamento de atores (CRUD).
- Gerenciamento de classes (CRUD).
- Gerenciamento de diretores (CRUD).
- Gerenciamento de itens (CRUD).
- Gerenciamento de títulos (CRUD).
- Gerenciamento de filmes (CRUD).
- Gerenciamento de clientes (CRUD).
- Registro e devolução de locações.

---

## Tecnologias Utilizadas

- **Spring Boot**: Framework para construção da API.
- **Hibernate**: ORM para interação com o banco de dados.
- **MySQL**: Banco de dados relacional.
- **Lombok**: Redução de boilerplate no código.

---

## Configuração do Ambiente

1. Certifique-se de ter o **Java 17** ou superior e o **Maven** instalados.
2. Configure o banco de dados MySQL:
   - Crie um banco de dados chamado `locadora`.
   - Atualize o arquivo `application.properties` ou `application.yml` com as credenciais do banco:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/locadora
     spring.datasource.username=seu-usuario
     spring.datasource.password=sua-senha
     ```
3. Clone este repositório:
   ```bash
   git clone https://github.com/seu-usuario/backend-locadora.git
   cd backend-locadora
