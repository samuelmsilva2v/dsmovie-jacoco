# DSMovie — Cobertura com JaCoCo

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
