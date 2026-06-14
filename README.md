# Sistema Automotivo — Gestão de Estoque de Veículos

API REST para gestão de estoque de veículos de uma concessionária, desenvolvida em
**Java + Spring Boot** com persistência em **MySQL**. Projeto da disciplina de
**Programação Orientada a Objetos — UniFECAF**.

Permite cadastrar **marcas**, **modelos** e **veículos** (carros, motos e caminhões),
consultar com filtros dinâmicos, atualizar e remover — um CRUD completo aplicando os
pilares de POO.

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 (LTS) |
| Framework | Spring Boot 3.3 |
| Persistência | Spring Data JPA / Hibernate |
| Banco de dados | MySQL 8 |
| Migrations | Flyway |
| Validação | Bean Validation (Jakarta) |
| Documentação | springdoc-openapi (Swagger UI) |
| Testes | JUnit 5, Mockito, H2 |
| Build | Maven (via Maven Wrapper) |

**Por que estas tecnologias?** Java + Spring Boot é o padrão de mercado para APIs REST
corporativas, com ecossistema maduro (injeção de dependências, JPA, validação). MySQL é
um banco relacional robusto e amplamente adotado. Flyway garante um schema versionado e
reprodutível. Swagger documenta e permite testar a API pelo navegador.

---

## Conceitos de POO aplicados

- **Herança e classe abstrata:** `Veiculo` é abstrata; `Carro`, `Moto` e `Caminhao`
  a estendem (estratégia JPA `JOINED`, refletida no banco).
- **Polimorfismo:** o método `tipo()` é resolvido por cada subclasse; coleções são
  tratadas como `Veiculo`; os DTOs de entrada também são polimórficos (campo `tipo`).
- **Encapsulamento:** atributos privados expostos por getters/setters; regras como
  `isImutavel()` (veículo vendido não pode ser alterado). *Sem Lombok, de propósito,
  para deixar o encapsulamento visível.*
- **Associações:** `Marca` 1—N `Modelo` 1—N `Veiculo`.

---

## Arquitetura em camadas

```
Controller (REST)  ->  DTO + Bean Validation
   |
Service (interface + impl, regras de negócio)
   |
Repository (Spring Data JPA + Specifications p/ filtros)
   |
Entity (hierarquia Veiculo)  ->  MySQL (via Flyway)
```

Pacotes: `domain` (entidades/enums), `dto`, `mapper`, `repository`, `specification`,
`service`(+`impl`), `controller`, `exception`, `config`.

---

## Pré-requisitos

- **JDK 21** — `java -version` deve indicar 21.
- **MySQL 8** rodando em `localhost:3306`.
- Maven **não** é necessário: use o wrapper `./mvnw` (Linux/Mac) ou `mvnw.cmd` (Windows).

### Banco de dados

Crie o banco (ou deixe a aplicação criar, graças a `createDatabaseIfNotExist=true`):

```sql
CREATE DATABASE IF NOT EXISTS sistema_automotivo;
```

Credenciais padrão em `application.yml`: usuário `root`, senha `root`. Para usar outra
senha, defina a variável de ambiente `DB_PASSWORD`.

O **Flyway** cria o schema e insere dados de exemplo automaticamente na primeira execução
(`src/main/resources/db/migration`).

---

## Como executar

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

### Rodar os testes

```bash
./mvnw test
```

Os testes usam **H2 em memória** (perfil `test`), portanto **não exigem MySQL**.

---

## Endpoints principais

### Marcas — `/api/marcas`
| Método | Caminho | Descrição |
|---|---|---|
| POST | `/api/marcas` | Cria marca |
| GET | `/api/marcas` | Lista marcas |
| GET | `/api/marcas/{id}` | Busca por id |
| PUT | `/api/marcas/{id}` | Atualiza |
| DELETE | `/api/marcas/{id}` | Remove |

### Modelos — `/api/modelos`
| Método | Caminho | Descrição |
|---|---|---|
| POST | `/api/modelos` | Cria modelo |
| GET | `/api/modelos?marcaId=` | Lista (filtro opcional por marca) |
| GET | `/api/modelos/{id}` | Busca por id |
| PUT | `/api/modelos/{id}` | Atualiza |
| DELETE | `/api/modelos/{id}` | Remove |

### Veículos — `/api/veiculos`
| Método | Caminho | Descrição |
|---|---|---|
| POST | `/api/veiculos` | Cadastra veículo (carro/moto/caminhão) |
| GET | `/api/veiculos` | Consulta com filtros e paginação |
| GET | `/api/veiculos/{id}` | Busca por id |
| PATCH | `/api/veiculos/{id}` | Atualiza preço/quilometragem/status |
| DELETE | `/api/veiculos/{id}` | Remove |

**Filtros de consulta** (query params): `marca`, `modelo`, `precoMin`, `precoMax`,
`ano`, `status`, `tipo`, além de `page`, `size`, `sort` (paginação).

---

## Exemplos (curl)

```bash
# Criar marca
curl -X POST http://localhost:8080/api/marcas \
  -H "Content-Type: application/json" -d '{"nome":"Toyota"}'

# Criar modelo (marcaId = id retornado acima)
curl -X POST http://localhost:8080/api/modelos \
  -H "Content-Type: application/json" -d '{"nome":"Corolla","marcaId":1}'

# Cadastrar um carro
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" -d '{
    "tipo":"CARRO","modeloId":1,"anoFabricacao":2022,"cor":"Preto",
    "preco":120000,"quilometragem":15000,"status":"DISPONIVEL",
    "numeroPortas":4,"tipoCombustivel":"Flex"
  }'

# Consultar com filtros
curl "http://localhost:8080/api/veiculos?status=DISPONIVEL&tipo=CARRO&precoMax=200000"

# Atualizar preço e status
curl -X PATCH http://localhost:8080/api/veiculos/1 \
  -H "Content-Type: application/json" -d '{"preco":99000,"status":"RESERVADO"}'

# Remover
curl -X DELETE http://localhost:8080/api/veiculos/1
```

Para **moto** use `"tipo":"MOTO"` com `cilindrada`; para **caminhão**,
`"tipo":"CAMINHAO"` com `capacidadeCargaKg` e `numeroEixos`.

---

## Estrutura do projeto

```
src/main/java/com/unifecaf/sistemaautomotivo
 ├── config/         OpenApiConfig
 ├── controller/     MarcaController, ModeloController, VeiculoController
 ├── domain/         Marca, Modelo, Veiculo (abstrata), Carro, Moto, Caminhao, StatusVeiculo
 ├── dto/            marca/, modelo/, veiculo/ (requests polimórficos e responses)
 ├── exception/      ApiError, ResourceNotFoundException, BusinessException, GlobalExceptionHandler
 ├── mapper/         MarcaMapper, ModeloMapper, VeiculoMapper
 ├── repository/     MarcaRepository, ModeloRepository, VeiculoRepository
 ├── service/        interfaces + impl/
 └── specification/  VeiculoSpecifications (filtros dinâmicos)
src/main/resources
 ├── application.yml, application-test.yml
 └── db/migration/   V1__schema.sql, V2__seed.sql
```
