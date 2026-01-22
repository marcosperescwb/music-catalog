# Music Catalog API - Desafio Técnico Sênior

API REST desenvolvida como parte do processo seletivo para vaga de Desenvolvedor Java Sênior (SEPLAG/MT). O projeto gerencia um catálogo de artistas e álbuns, implementando requisitos complexos de integração, armazenamento de arquivos e sincronização de dados.

## 🚀 Tecnologias Utilizadas

*   **Java 17 (LTS)**
*   **Spring Boot 3.x** (Web, Data JPA, Security, Actuator, WebSocket)
*   **PostgreSQL 15** (Banco de Dados Relacional)
*   **MinIO** (Object Storage compatível com AWS S3)
*   **Flyway** (Migration e Versionamento de Banco)
*   **MapStruct** (Mapeamento de Objetos de alta performance)
*   **Bucket4j** (Rate Limiting - Token Bucket Algorithm)
*   **Auth0 Java JWT** (Autenticação Stateless)
*   **SpringDoc OpenAPI** (Documentação Swagger)
*   **Docker & Docker Compose** (Orquestração de Ambiente)

---

## 🏗️ Arquitetura

O projeto foi estruturado seguindo os princípios da **Clean Architecture** (adaptada para Arquitetura Hexagonal/Ports and Adapters). O objetivo é isolar o domínio da aplicação de detalhes de infraestrutura.

### Estrutura de Pacotes
*   `core`: Contém as Regras de Negócio puras.
    *   `domain`: Entidades de negócio (POJOs).
    *   `usecase`: Implementação das regras (Services).
    *   `port`: Interfaces que definem as