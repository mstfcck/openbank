# Project Structure

## Overview

This project structure is designed for large-scale, enterprise-level Spring Boot applications for development teams while maintaining code quality, scalability, reliability and maintainability.

## Structure

Project folder and file structures must conform to the following scheme.

```
openbank-microservices/
├── gateway/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └── openbank/
│   │       │           └── gateway/
│   │       │               └── GatewayApplication.java
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
├── account-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── openbank/
│   │   │   │           └── accountservice/
│   │   │   │               ├── AccountServiceApplication.java
│   │   │   │               ├── controller/
│   │   │   │               │   └── AccountController.java
│   │   │   │               ├── DTOs/
│   │   │   │               │   └── AccountRequest.java
│   │   │   │               │   └── AccountResponse.java
│   │   │   │               │   └── validator/
│   │   │   │               │       └── AccountRequestValidator.java
│   │   │   │               ├── model/
│   │   │   │               │   └── Account.java
│   │   │   │               ├── repository/
│   │   │   │               │   └── AccountRepository.java
│   │   │   │               ├── service/
│   │   │   │               │   └── AccountService.java
│   │   │   │               └── client/
│   │   │   │                   └── UserServiceClient.java
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── openbank/
│   │                   └── accountservice/
│   │                       └── AccountServiceTests.java
│   └── pom.xml
├── transaction-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── openbank/
│   │   │   │           └── transactionservice/
│   │   │   │               ├── TransactionServiceApplication.java
│   │   │   │               ├── controller/
│   │   │   │               │   └── TransactionController.java
│   │   │   │               ├── DTOs/
│   │   │   │               │   └── TransactionRequest.java
│   │   │   │               │   └── TransactionResponse.java
│   │   │   │               │   └── validator/
│   │   │   │               │       └── TransactionRequestValidator.java
│   │   │   │               ├── model/
│   │   │   │               │   └── Transaction.java
│   │   │   │               ├── repository/
│   │   │   │               │   └── TransactionRepository.java
│   │   │   │               ├── service/
│   │   │   │               │   └── TransactionService.java
│   │   │   │               └── client/
│   │   │   │                   ├── AccountServiceClient.java
│   │   │   │                   └── UserServiceClient.java
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   └── pom.xml
├── user-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── openbank/
│   │   │   │           └── userservice/
│   │   │   │               ├── UserServiceApplication.java
│   │   │   │               ├── controller/
│   │   │   │               │   └── UserController.java
│   │   │   │               ├── DTOs/
│   │   │   │               │   └── UserRequest.java
│   │   │   │               │   └── UserResponse.java
│   │   │   │               │   └── validator/
│   │   │   │               │       └── UserRequestValidator.java
│   │   │   │               ├── model/
│   │   │   │               │   └── User.java
│   │   │   │               ├── repository/
│   │   │   │               │   └── UserRepository.java
│   │   │   │               └── service/
│   │   │   │                   └── UserService.java
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   └── pom.xml
├── discovery-service/
│   └── (Service discovery implementation)
├── config-service/
│   └── (Configuration server implementation)
├── docker-compose.yml
└── pom.xml (Parent POM)
```
