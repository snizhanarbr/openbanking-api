# OpenBanking API
A Spring Boot application demonstrating backend development skills through a simple OpenBanking API. The system allows users to fetch account balances, view recent transactions, and initiate payments while interacting with a mocked external banking service.

# Projects Overview
This project simulates a basic OpenBanking system with RESTful APIs:
- **Retrieve account balances**
- **Fetch the last 10 transactions**
- **Initiate IBAN-to-IBAN payments**
- **Simulated external banking service (mock API)**
- **Payment validation and status management**

## Technologies
- **Java 17 & Spring Boot 3**
- **Spring Data JPA with H2 in-memory database**
- **REST API design**
- **Lombok for boilerplate reduction**
- **JUnit & Mockito for unit and integration testing**
- **Swagger/OpenAPI documentation**
- **RestTemplate for external service integration**

## Features
- **Account APIs:**  GET `/api/accounts/{iban}/balance`, GET `/api/accounts/{iban}/transactions`
- **Payment API:**  POST `/api/payments/initiate` with validation for sufficient funds
- **External Bank Mock:**  `/mock-bank` endpoints to simulate PSD2/OpenBanking responses
- **Payment Status Handling:**  `PENDING`, `COMPLETED`, `FAILED`
- **Error Handling:**  Global exception handler for insufficient funds and unexpected errors
- **Testing:**  Unit and integration tests for services and controllers

## Quick Start
- Clone the repository
- Build and run the application: `./mvnw spring-boot:run`
- API available at `http://localhost:8080`
- Swagger UI for API documentation: `http://localhost:8080/swagger-ui/index.html`

## Endpoints
**Accounts**
- GET /api/accounts/{iban}/balance — Get current balance
- GET /api/accounts/{iban}/transactions — Get last 10 transactions
**Payments**
- POST /api/payments/initiate — Initiate new IBAN-to-IBAN payment
**Mock External Bank**
- GET /mock-bank/accounts/{iban}/balance
- GET /mock-bank/accounts/{iban}/transactions
- POST /mock-bank/payments
