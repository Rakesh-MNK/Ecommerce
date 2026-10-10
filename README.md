# E-Commerce Microservices — Exactly 2 Services

This portfolio project intentionally contains **only two separately buildable Maven projects**:

1. `EurekaService` — Eureka Server on `8761`
2. `APIGatewayService` — Spring Cloud Gateway on `8080`

There is **no AuthService, ProductService, OrderService, InventoryService, PaymentService, NotificationService, Kafka, MongoDB, Docker, Docker Compose, Kubernetes, or multi-module Maven parent**.

## Technology

- Java 17
- Spring Boot 3.4.5
- Spring Cloud 2024.0.2
- Maven
- Spring Web (Eureka REST demo endpoint)
- Spring Cloud Gateway / WebFlux
- Spring Security 6
- JWT with JJWT 0.12.6
- Eureka Server + Eureka Client
- Jakarta Bean Validation
- Java Records
- Global exception handling
- JUnit 5
- Mockito
- Postman

### Why MySQL/JPA/Hibernate are not forced into these two services

JPA/Hibernate/MySQL are persistence technologies, but neither a service registry nor a reactive API Gateway needs a business database in this two-service design. Adding a blocking JPA stack to the Gateway just to satisfy a checklist would make the architecture less clean and can introduce WebFlux/JPA blocking concerns. The project therefore keeps the two services focused and runnable.

## Local prerequisites

- JDK 17
- Maven 3.9+
- No Docker required
- No MySQL required for this exact two-service scope

## Start

### Terminal 1 — EurekaService

```text
cd EurekaService
mvn clean test
mvn spring-boot:run
```

Open: http://localhost:8761

### Terminal 2 — APIGatewayService

```text
cd APIGatewayService
mvn clean test
mvn spring-boot:run
```

Open: http://localhost:8080/actuator/health

The Gateway registers itself in Eureka. Because there is no third service, the Gateway demo route calls the Eureka server directly at `http://localhost:8761`.

## End-to-end requests

### 1. Gateway public endpoint

`GET http://localhost:8080/api/gateway/public`

### 2. Gateway → Eureka route

`GET http://localhost:8080/api/eureka/status`

Expected shape:

```json
{
  "service": "EUREKASERVICE",
  "status": "UP",
  "message": "Eureka Server is running locally",
  "timestamp": "..."
}
```

### 3. Protected Gateway route

The gateway has a JWT validation demo route:

`GET http://localhost:8080/api/gateway/validate-token`

Header:

```text
Authorization: Bearer <JWT>
```

There is intentionally no AuthService in this project, so this repository does not issue JWTs. For local testing, run `python scripts/generate_test_jwt.py` and copy the printed token into the Postman `token` variable.

### 4. Protected Gateway → Eureka route

`GET http://localhost:8080/api/protected/eureka/status`

Header:

```text
Authorization: Bearer <JWT>
```

The gateway validates the token and then forwards the request to Eureka.

## Architecture

```text
                    +----------------------+
                    |    EurekaService     |
                    | Spring Cloud Eureka  |
                    |      :8761           |
                    +----------+-----------+
                               ^
                               | Eureka Client registration
                               |
                    +----------+-----------+
Client/Postman ---> |  APIGatewayService   |
                    | Spring Cloud Gateway |
                    | Spring Security/JWT  |
                    |      :8080           |
                    +----------------------+
                               |
                               +---- direct demo route ---> Eureka :8761
```

## Important portfolio note

A production e-commerce platform normally has downstream business services. This repository deliberately does not create them because the requested scope is exactly two services. The Gateway is therefore demonstrated with a real route to the Eureka service plus a local JWT validation endpoint.
