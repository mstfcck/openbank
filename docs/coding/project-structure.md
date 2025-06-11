# Project Structure

## Overview

This project structure is designed for large-scale, enterprise-level Spring Boot applications for development teams while maintaining code quality, scalability, reliability and maintainability.

## Complete Project Structure

Project folder and file structures must conform to the following scheme.

```
openbank-microservices/
├── api-gateway/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └── openbank/
│   │       │           └── gateway/
│   │       │               └── ApiGatewayApplication.java
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
│   │   │   │               ├── model/
│   │   │   │               │   └── AccountRequest.java
│   │   │   │               │   └── AccountResponse.java
│   │   │   │               │   └── validator/
│   │   │   │               │       └── AccountRequestValidator.java
│   │   │   │               ├── entity/
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
│   │   │   │               ├── model/
│   │   │   │               │   └── TransactionRequest.java
│   │   │   │               │   └── TransactionResponse.java
│   │   │   │               │   └── validator/
│   │   │   │               │       └── TransactionRequestValidator.java
│   │   │   │               ├── entity/
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
│   │   │   │               ├── model/
│   │   │   │               │   └── UserRequest.java
│   │   │   │               │   └── UserResponse.java
│   │   │   │               │   └── validator/
│   │   │   │               │       └── UserRequestValidator.java
│   │   │   │               ├── entity/
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
└── parent-pom.xml (Optional parent POM)
```
