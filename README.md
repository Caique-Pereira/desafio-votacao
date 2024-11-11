
# Sistema de Votação em Cooperativas

Este projeto é um exemplo de aplicação para gerenciar votações em cooperativas. Ele permite que os associados participem de decisões tomadas em assembleias, com cada membro tendo direito a um voto. A aplicação foi desenvolvida utilizando Java e Spring Boot, oferecendo uma API REST para gerenciar pautas, sessões e votos.

---

## Funcionalidades

- Registro de novas pautas para votação.
- Abertura de sessões de votação vinculadas a uma pauta específica com data e hora de encerramento.
- Registro e processamento de votos enviados pelos associados.
- Cálculo dos resultados das votações realizadas e exibição de contagem de votos.

---

## Tecnologias Utilizadas

- **Java 21**: Linguagem principal para o desenvolvimento.
- **Spring Boot 2.5**: Framework para criação de APIs REST.
- **Lombok**: Redução de código repetitivo no projeto.
- **H2 Database**: Banco de dados em memória para testes e desenvolvimento.
- **Spring Data JPA**: Abstração para acesso ao banco de dados.
- **Mockito**: Ferramenta para criação de testes unitários com mocks.

---

## Instalação

1. Clone o repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd <NOME_DO_PROJETO>
   ```

2. Compile e execute o projeto usando Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. A aplicação estará disponível em:
   ```plaintext
   http://localhost:8080
   ```

---

## Endpoints da API

### 1. **Cadastrar um novo tópico (pauta)**

- **URL:** `/topics`
- **Método:** `POST`
- **Cabeçalhos:** `Content-Type: application/json`
- **Body (JSON):**
  ```json
  {
    "description": "Descrição da pauta"
  }
  ```
- **Resposta Exemplo:**
  ```json
  {
    "id": 1,
    "description": "Descrição da pauta"
  }
  ```

---

### 2. **Abrir uma sessão de votação**

- **URL:** `/sessions`
- **Método:** `POST`
- **Cabeçalhos:** `Content-Type: application/json`
- **Body (JSON):**
  ```json
  {
    "topic": 1,
    "finalDatetime": "2024-12-01T18:00:00"
  }
  ```
- **Resposta Exemplo:**
  ```json
  {
    "id": 1,
    "topic": {
      "id": 1,
      "description": "Descrição da pauta"
    },
    "finalDatetime": "2024-12-01T18:00:00"
  }
  ```

---

### 3. **Registrar votos dos associados**

- **URL:** `/voting/{topicId}`
- **Método:** `POST`
- **Cabeçalhos:** `Content-Type: application/json`
- **Body (JSON):**
  ```json
  {
    "vote": true,
    "associatedId": 1001
  }
  ```
- **Resposta Exemplo:**
  ```json
  {
    "id": 1,
    "vote": true,
    "associatedId": 1001,
    "topicId": 1
  }
  ```

---

### 4. **Contabilizar os votos e fornecer o resultado da votação**

- **URL:** `/topics/{topicId}/results`
- **Método:** `GET`
- **Resposta Exemplo:**
  ```json
  {
    "YES": 10,
    "NO": 5
  }
  ```

---

## Testes

- **Unit Tests:** Desenvolvidos com JUnit 5 e Mockito para garantir a qualidade do código.
- **Cobertura:** Testes cobrem cenários de sucesso e exceções, como sessão encerrada, pauta inexistente e voto duplicado.

---

Se precisar de mais informações ou de suporte para configurar o ambiente, entre em contato!
