# API Registro Meteorológico

API REST para **registro e gerenciamento de medições meteorológicas**, desenvolvida para o CP2 (2026/02 – Modelo 1) da disciplina *Microservice and Web Engineering & IT Services*.

| | |
|---|---|
| **Aluno** | Pietro – RM 557000 |
| **Group** | `com.github.pietropn` |
| **Artifact** | `api-registro-pietro-557000-m1` |

## Tecnologias

- Java 25 · Spring Boot 4.0.8 · Maven
- Spring Web MVC, Spring Data JPA, Bean Validation
- H2 (profile `test`) e MySQL 8 (profile `dev`)
- Flyway (migrations versionadas)
- springdoc-openapi (Swagger UI)
- Docker (Dockerfile multi-stage)

## Estrutura do projeto

```
src/main/java/com/github/pietropn/apiregistro
├── config/        OpenApiConfig (metadados do Swagger)
├── controller/    RegistroMeteorologicoController (endpoints REST)
├── dto/           RequestDTO (entrada + validações) e ResponseDTO (saída)
├── entity/        RegistroMeteorologico e enum CondicaoClimatica
├── exception/     Tratamento global de erros (400 / 404)
├── repository/    RegistroMeteorologicoRepository (JpaRepository)
└── service/       Regras de negócio
src/main/resources
├── application.properties        configurações gerais
├── application-test.properties   profile test (H2)
├── application-dev.properties    profile dev (MySQL)
└── db/migration
    ├── V1__create_table_registro_meteorologico.sql
    └── V2__insert_registros_iniciais.sql   (3 registros iniciais)
```

## Modelo e validações

| Campo | Tipo | Regras |
|---|---|---|
| `registroId` | Long | gerado automaticamente |
| `estacaoNome` | String | obrigatório, 3 a 100 caracteres |
| `dataRegistro` | LocalDate | obrigatório, menor ou igual à data atual |
| `temperatura` | BigDecimal | obrigatório |
| `percentualUmidade` | BigDecimal | obrigatório, entre 0 e 100 |
| `precipitacao` | BigDecimal | obrigatório, maior ou igual a 0 |
| `condicaoClimatica` | Enum | obrigatório: `ENSOLARADO`, `NUBLADO`, `CHUVOSO`, `TEMPESTADE` |

## Endpoints

Base: `/api/registros`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| GET | `/api/registros` | Lista todos os registros | 200 |
| GET | `/api/registros/{id}` | Busca por id | 200 / 404 |
| POST | `/api/registros` | Cadastra um registro | 201 / 400 |
| PUT | `/api/registros/{id}` | Atualiza um registro | 200 / 400 / 404 |
| DELETE | `/api/registros/{id}` | Exclui um registro | 204 / 404 |

Exemplo de corpo (POST/PUT):

```json
{
  "estacaoNome": "Estação Mirante de Santana - SP",
  "dataRegistro": "2026-10-14",
  "temperatura": 25.30,
  "percentualUmidade": 60.00,
  "precipitacao": 0.00,
  "condicaoClimatica": "NUBLADO"
}
```

---

## Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) instalado e em execução
- (Opcional, para rodar sem Docker) Java 25 e Maven 3.9+

Todos os comandos abaixo devem ser executados na **raiz do projeto** (onde está o `Dockerfile`).

## 1. Construir a imagem

```bash
docker build -t api-registro-pietro-557000-m1 .
```

O build tem dois estágios: o primeiro (`maven:3.9-eclipse-temurin-25`) compila e gera o `.jar`; o segundo (`eclipse-temurin:25-jre-alpine`) contém apenas o JRE e o `.jar`, gerando uma imagem final menor e executada por um usuário sem privilégios de root.

## 2. Executar com o profile `test` (H2 em memória)

```bash
docker run -d --name api-registro-test -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=test \
  api-registro-pietro-557000-m1
```

- O Flyway cria a tabela e insere os 3 registros iniciais no H2.
- Console do H2: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:registrodb` · Usuário: `sa` · Senha: *(vazia)*

Para parar e remover:

```bash
docker rm -f api-registro-test
```

## 3. Iniciar o MySQL

Crie uma rede para que a API e o banco se comuniquem pelo nome do container:

```bash
docker network create registro-net
```

Suba o MySQL:

```bash
docker run -d --name mysql-registro --network registro-net -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=registrodb \
  mysql:8.4
```

Aguarde o banco ficar pronto (acompanhe até aparecer `ready for connections`):

```bash
docker logs -f mysql-registro
```

## 4. Executar com o profile `dev` (MySQL)

```bash
docker run -d --name api-registro-dev --network registro-net -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e DB_HOST=mysql-registro \
  -e DB_PORT=3306 \
  -e DB_NAME=registrodb \
  -e DB_USER=root \
  -e DB_PASSWORD=root \
  api-registro-pietro-557000-m1
```

Na primeira execução o Flyway aplica `V1` (tabela) e `V2` (carga inicial) no MySQL. Para conferir:

```bash
docker exec -it mysql-registro mysql -uroot -proot registrodb \
  -e "SELECT * FROM tb_registro_meteorologico; SELECT version, description, success FROM flyway_schema_history;"
```

Para encerrar tudo:

```bash
docker rm -f api-registro-dev mysql-registro
docker network rm registro-net
```

> Se o container `api-registro-test` estiver rodando, pare-o antes, pois ambos usam a porta 8080.

## 5. Acessar o Swagger / OpenAPI

Com a aplicação em execução (qualquer profile):

- Swagger UI: http://localhost:8080/swagger-ui.html
- Especificação OpenAPI (JSON): http://localhost:8080/v3/api-docs

## Executar sem Docker (opcional)

```bash
# profile test (H2)
mvn spring-boot:run -Dspring-boot.run.profiles=test

# profile dev (MySQL rodando em localhost:3306, usuário root / senha root)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Testes rápidos com cURL

```bash
# listar
curl http://localhost:8080/api/registros

# criar
curl -X POST http://localhost:8080/api/registros \
  -H "Content-Type: application/json" \
  -d '{"estacaoNome":"Estação Curitiba - PR","dataRegistro":"2026-10-13","temperatura":16.2,"percentualUmidade":77,"precipitacao":4.5,"condicaoClimatica":"NUBLADO"}'

# exemplo de erro de validação (umidade > 100 e data futura) -> 400
curl -X POST http://localhost:8080/api/registros \
  -H "Content-Type: application/json" \
  -d '{"estacaoNome":"AB","dataRegistro":"2099-01-01","temperatura":20,"percentualUmidade":150,"precipitacao":-1,"condicaoClimatica":"NUBLADO"}'
```
