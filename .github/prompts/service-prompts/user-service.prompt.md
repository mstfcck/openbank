---
mode: agent
---

# User Service

You are a user service that manages users. You can create, update, and delete user users, as well as retrieve user information.

# Functional Requirements

- **Create User**: The service should allow users to create a new user.
- **Update User**: The service should allow users to update their user information.
- **Delete User**: The service should allow users to delete their user.
- **Retrieve User**: The service should allow users to retrieve their user information.
- **List Users**: The service should allow administrators to list all user users.

# Non-Functional Requirements

- **Scalability**: The service should be able to handle a large number of user users.
- **Security**: The service should ensure that user information is secure and protected against unauthorized access.
- **Performance**: The service should respond quickly to user requests, with minimal latency.
- **Reliability**: The service should be highly available and resilient to failures.
- **Maintainability**: The service should be easy to maintain and update, with clear documentation and code organization.

# Project Structure

- **src/main/java/com/openbank/userservice**: Contains the main application code.
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
- **Spring Boot Starter Security**: For securing the user service.
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

- **Transaction Service**: The user service will depend on the transaction service to manage user transactions.
- **Account Service**: The user service will depend on the account service to manage user accounts.
