# API Registro Meteorológico

API REST para registro e gerenciamento de medições meteorológicas.

**Aluno:** Pietro – RM 557000

**Tecnologias:** Java 25 · Spring Boot 4 · H2 · MySQL · Flyway · Swagger · Docker

---

## 1. Construir a imagem

Na raiz do projeto:

```bash
docker build -t api-registro-pietro-557000-m1 .
```

## 2. Executar com o profile `test` (H2)

```bash
docker run -d --name api-test -p 8080:8080 -e SPRING_PROFILES_ACTIVE=test api-registro-pietro-557000-m1
```

Console do H2: http://localhost:8080/h2-console
(JDBC URL: `jdbc:h2:mem:registrodb` · usuário: `sa` · senha: vazia)

Para parar: `docker rm -f api-test`

## 3. Iniciar o MySQL

```bash
docker network create registro-net

docker run -d --name mysql-registro --network registro-net -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=registrodb mysql:8.4
```

Aguarde alguns segundos até o MySQL ficar pronto.

## 4. Executar com o profile `dev` (MySQL)

```bash
docker run -d --name api-dev --network registro-net -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev -e DB_HOST=mysql-registro api-registro-pietro-557000-m1
```

> Pare o container `api-test` antes, pois os dois usam a porta 8080.

Para parar: `docker rm -f api-dev mysql-registro`

## 5. Acessar o Swagger

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI (JSON): http://localhost:8080/v3/api-docs

---

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/registros` | Lista todos |
| GET | `/api/registros/{id}` | Busca por id |
| POST | `/api/registros` | Cadastra |
| PUT | `/api/registros/{id}` | Atualiza |
| DELETE | `/api/registros/{id}` | Exclui |

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

`condicaoClimatica`: `ENSOLARADO`, `NUBLADO`, `CHUVOSO` ou `TEMPESTADE`.

## Migrations (Flyway)

- `V1__create_table_registro_meteorologico.sql` – cria a tabela
- `V2__insert_registros_iniciais.sql` – insere 3 registros iniciais