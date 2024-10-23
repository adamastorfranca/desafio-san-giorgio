
# Payment Confirmation Service

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-green)
![AWS SQS](https://img.shields.io/badge/AWS%20SQS-LocalStack-yellow)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)

## Descrição do Desafio

Este projeto foi desenvolvido como parte de um desafio para avaliar a habilidade de implementar uma funcionalidade de confirmação de pagamentos em Java. A aplicação recebe um objeto contendo o código do vendedor e uma lista de pagamentos realizados, valida a existência do vendedor e das cobranças e determina se o pagamento é parcial, total ou excedente. Cada tipo de pagamento é então enviado para uma fila SQS específica.

## Requisitos Funcionais

- **Recepção de Dados:** Receber um objeto contendo o código do vendedor e uma lista de pagamentos.
- **Validação de Dados:** Verificar a existência do vendedor e das cobranças, e validar os valores de pagamentos.
- **Processamento de Pagamentos:** Determinar o status (parcial, total, excedente) de cada pagamento.
- **Integração com SQS:** Enviar os pagamentos para filas SQS distintas com base no status.
- **Retorno de Resposta:** Retornar o objeto com o status atualizado de cada pagamento.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **AWS SDK para Java (usando LocalStack)**
- **PostgreSQL**
- **Docker & Docker Compose**
- **JUnit & Mockito (para testes unitários)**

## Estrutura do Projeto

```plaintext
san-giorgio-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/desafio/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── domain/
│   │   │       ├── exception/
│   │   │       ├── listener/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── util/
│   │   └── resources/
│   │       ├── application.yml
│   ├── test/
│   │   ├── java/
│   │   │   └── br/com/desafio/
│   │   │       └── service/
│   │   └── resources/
│   │       └── application-test.yml
├── docker-compose.yml
├── build.gradle
└── README.md
```

## Como Executar o Projeto Localmente

### Pré-requisitos

- **Java 17+**
- **Docker e Docker Compose**
- **Gradle**

### Passo a Passo

1. **Clone o repositório**

    ```bash
    git clone https://github.com/adamastorfranca/payment-confirmation.git
    cd payment-confirmation
    ```

2. **Configure o Ambiente com Docker Compose**

    A aplicação usa Docker Compose para iniciar um contêiner PostgreSQL e um LocalStack para simular o serviço SQS.

    ```bash
    docker-compose up -d
    ```

3. **Compile e Execute a Aplicação**

    ```bash
    ./gradlew clean build
    ./gradlew bootRun
    ```

4. **Verifique se a aplicação está funcionando**

    A aplicação estará disponível em:

    ```
    http://localhost:8080
    ```

### Configuração Padrão

**application.yml**:

```yaml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:postgresql://localhost:5432/financial
    username: dummy
    password: dummy
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
  aws:
    sqs:
      endpoint: http://localhost:4566
      region: us-east-1
      access-key: dummy
      secret-key: dummy

logging:
  level:
    root: INFO
```

## Testes

### Executar Testes Unitários

Para garantir que todas as funcionalidades estão funcionando como esperado, utilize:

```bash
./gradlew test
```

## Licença

Este projeto é licenciado sob a [MIT License](LICENSE).

## Contato

- **Autor:** Adamastor Franca
- **E-mail:** adamastorfranca@hotmail.com.com
- **LinkedIn:** [Adamastor Franca](https://www.linkedin.com/in/adamastor-franca/)