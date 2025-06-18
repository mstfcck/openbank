---
mode: agent
---

# Account Service

You are an account service that manages users' bank accounts. You can create, update, and delete the accounts, as well as retrieve account information.

# Functional Requirements

- **Create Account**: The service should allow users to create a new bank account.
- **Update Account**: The service should allow users to update their account information.
- **Delete Account**: The service should allow users to delete their bank account.
- **Retrieve Account**: The service should allow users to retrieve their account information.
- **List Accounts**: The service should allow users to list all their bank accounts.
- **Account Balance**: The service should allow users to check their account balance.
- **Account Statements**: The service should allow users to generate account statements.
- **Account Notifications**: The service should notify users of important account events (e.g., balance changes).

# Non-Functional Requirements

- **Scalability**: The service should be able to handle a large number of accounts.
- **Security**: The service should ensure that account information is secure and protected against unauthorized access.
- **Performance**: The service should respond quickly to user requests, with minimal latency.
- **Reliability**: The service should be highly available and resilient to failures.
- **Maintainability**: The service should be easy to maintain and update, with clear documentation and code organization.

# Project Structure

- **src/main/java/com/openbank/accountservice**: Contains the main application code.
  - **controller**: Contains REST controllers for handling HTTP requests.
  - **service**: Contains business logic and service classes.
  - **repository**: Contains data access layer classes (e.g., JPA repositories).
  - **model**: Contains domain models and entities.
  - **DTOs**: Contains Data Transfer Objects for transferring data between layers.
  - **mapper**: Contains mappers for converting between entities and DTOs.
  - **exception**: Contains custom exceptions and global exception handlers.
  - **security**: Contains security configurations and authentication classes.
  - **util**: Contains utility classes and helper methods.
  - **config**: Contains configuration classes (e.g., security, database).
- **src/main/resources**: Contains application configuration files (e.g., application.properties).
- **src/test/java/com/openbank/accountservice**: Contains unit and integration tests.
- **pom.xml**: Contains Maven dependencies and project configuration.

# Dependencies

- **Spring Boot Starter Web**: For building RESTful web services.
- **Spring Boot Starter Data JPA**: For data access using JPA.
- **Spring Boot Starter Security**: For securing the service.
- **Spring Boot Starter Test**: For testing the application.

# Best Practices

- **Use RESTful APIs**: Design the service using REST principles for better interoperability.
- **Use DTOs**: Use Data Transfer Objects (DTOs) to separate internal models from external representations.
- **Implement Exception Handling**: Use a global exception handler to manage errors and provide meaningful responses.
- **Use Logging**: Implement logging for debugging and monitoring purposes.
- **Write Tests**: Implement unit and integration tests to ensure code quality and reliability.

# Documentation

- **Functional Requirements**: [Functional Requirements](../../docs/business-demand/functional-requirements.md)
- **Non-Functional Requirements**: [Non-Functional Requirements](../../docs/business-demand/non-functional-requirements.md)
- **Project Structure**: [Project Structure](../../docs/coding/project-structure.md)

# Service Dependencies

- **User Service**: The account service will depend on the user service to retrieve user information when creating or updating accounts.
- **Transaction Service**: The account service will depend on the transaction service to handle transactions related to accounts (e.g., deposits, withdrawals).
