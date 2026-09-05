# DSMovie — Cobertura com JaCoCo
[🇺🇸 Read in English](#dsmovie--coverage-with-jacoco)

Aplicação **DSMovie** (filmes e avaliações, DevSuperior) com testes unitários da camada de serviço e cobertura de código via **JaCoCo**.

## Tecnologias
Java 25, Spring Boot 4.0.6, Spring Security + OAuth2 Authorization Server, H2, JUnit 5, Mockito, JaCoCo 0.8.14

## Testes
`MovieServiceTests`, `ScoreServiceTests` e `UserServiceTests` cobrem a camada de serviço. Controllers, entidades, DTOs e configurações ficam fora da análise de cobertura (excluídos no plugin do JaCoCo).

## Como executar
```bash
mvn verify
```
Roda os testes e gera o relatório em `target/jacoco-report/index.html`.

---

# DSMovie — Coverage with JaCoCo
[🇧🇷 Leia em Português](#dsmovie--cobertura-com-jacoco)

**DSMovie** application (movies and ratings, DevSuperior) with unit tests for the service layer and code coverage via **JaCoCo**.

## Technologies
Java 25, Spring Boot 4.0.6, Spring Security + OAuth2 Authorization Server, H2, JUnit 5, Mockito, JaCoCo 0.8.14

## Tests
`MovieServiceTests`, `ScoreServiceTests` and `UserServiceTests` cover the service layer. Controllers, entities, DTOs and config classes are excluded from the coverage analysis (excluded in the JaCoCo plugin).

## How to run
```bash
mvn verify
```
Runs the tests and generates the report at `target/jacoco-report/index.html`.
