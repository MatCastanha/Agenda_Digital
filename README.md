Membros:

#Matheus Castanha
#Giovani Barbosa
#Gustavo Araujo
#Gabriel Cerqueira
#João Carlos
#Thainara Branco



# 📞 Agenda de Contatos API RESTful

## 🌟 Propósito do Microserviço

Este projeto consiste em um **microserviço** Spring Boot 3 (Java 21) que gerencia uma agenda de contatos completa. Ele foi desenvolvido com foco em:

1.  **Conformidade RESTful:** Implementação das operações CRUD (Criar, Ler, Atualizar, Deletar) e rotas de busca (`/search`).
2.  **Testabilidade:** Cobertura de testes unitários superior a 90% nas camadas Controller e Service, utilizando Mockito.
3.  **Documentação:** Documentação interativa e automática usando **Springdoc OpenAPI**.

---

## 🛠️ Tecnologias e Configuração

| Componente | Versão / Detalhe |
| :--- | :--- |
| **Linguagem** | Java 21 (LTS) |
| **Framework** | Spring Boot 3.4.x |
| **Web / Docs** | Springdoc OpenAPI (Swagger UI) |
| **Persistência** | Spring Data JPA (Hibernate) |
| **Testes** | JUnit 5, Mockito, JaCoCo (Cobertura 90%+) |
| **Banco de Dados** | H2 (Dev) / MySQL (Produção) |

---

## 🚀 Instruções Detalhadas para Rodar Localmente

### Pré-requisitos
Para rodar o projeto, você deve ter instalado:
1.  **JDK 21** ou superior.
2.  **Maven** 3.6 ou superior.

### Configuração do Banco de Dados

O projeto utiliza o banco de dados **MySQL** por padrão.

1.  **Crie o banco de dados:**
    ```sql
    CREATE DATABASE agenda;
    ```
2.  **Verifique as credenciais** no arquivo `src/main/resources/application.properties` e ajuste-as conforme necessário:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/agenda?createDatabaseIfNotExist=true
    spring.datasource.username=seu_usuario_mysql
    spring.datasource.password=sua_senha_mysql
    ```
> **Nota:** Se preferir rodar com o banco de dados **H2 em memória**, comente as configurações do MySQL e utilize as configurações do H2 (geralmente já prontas no Starter Web).

### Comandos Maven

Navegue até o diretório raiz do projeto e execute os seguintes comandos:

| Objetivo | Comando Maven |
| :--- | :--- |
| **Instalação/Build** | `mvn clean install` |
| **Rodar a Aplicação** | `mvn spring-boot:run` |
| **Rodar Testes** | `mvn clean test` |
| **Verificar Cobertura** | `mvn clean install` (Relatório em `target/site/jacoco/index.html`) |

A aplicação iniciará na porta `8080`.

---

## 📚 Documentação e Uso da API

A documentação da API é gerada automaticamente pelo Springdoc OpenAPI.

### Acesso à Documentação Interativa

Após iniciar o servidor, a documentação Swagger UI estará disponível em:

👉 **`http://localhost:8080/swagger-ui.html`**



### Exemplos de Uso (cURL)

A seguir, exemplos de como interagir com os principais *endpoints* da API.

#### 1. Criar Novo Contato (`POST /contacts`)
```bash
curl -X POST "http://localhost:8080/contacts" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Maria Teste",
           "email": "maria@exemplo.com",
           "phone": "11999991234",
           "notes": "Cliente importado"
         }'
2. Buscar Contato por ID (GET /contacts/{id})
Bash

curl -X GET "http://localhost:8080/contacts/1"
3. Buscar por Nome (Filtro) (GET /contacts/search/name/{name})
Bash

# Busca contatos onde o nome contenha 'Maria'
curl -X GET "http://localhost:8080/contacts/search/name/Maria"
4. Atualizar Contato (PUT /contacts/{id})
Bash

curl -X PUT "http://localhost:8080/contacts/1" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Maria Silva",
           "email": "maria.silva@exemplo.com",
           "phone": "11999991234",
           "notes": "Dados atualizados"
         }'
5. Excluir Contato (DELETE /contacts/{id})
Bash

curl -X DELETE "http://localhost:8080/contacts/1"
