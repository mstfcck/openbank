# Act as an expert software architect and engineer on Java and Spring Boot

## Create a project suitable for microservice architecture with Spring best practices

### Non-Functional Requirements

Project:

- Builder: Maven
- Language: Java 24
- SDK Version: OpenSDK 24
- Framework: Spring Boot 3.4.5

Project Metadata:

- Group: com.openbank
- Artifact: [module-name]
- Name: [module-name]
- Description: Open Bank API
- Package name: com.openbank.[module-name]

Architecture and Best Practices:

- Microservice
- RESTful API
- Independently deployable
- Loosely coupled
- Database per Service - each service has its own private database
- Saga - use sagas, which a sequences of local transactions, to maintain data consistency across services
- Command-side replica - maintain a queryable replica of data in a service that implements a command
- API Composition - implement queries by invoking the services that own the data and performing an in-memory join
- CQRS - implement queries by maintaining one or more materialized views that can be efficiently queried
- Domain event - publish an event whenever data changes
- Event sourcing - persist aggregates as a sequence of events
- Create the folder structure appropriately since all microservices will be developed in the same project.
- There should be a separate "Shared" project for shared integrations to be used together and should be referenced by all other Microservice projects.
- All domains should be created as separate microservices.
- An API Gateway should be created for microservices.
- The entire project should be created by Spring best practices, and the entire structure should be built by microservice architecture.
- Create all the folders for the service structures based on the best practices, such as domain, service, database, etc.
- Implement Docker and Docker Compose for each project, which is ready for deployment.

### Functional Requirements

- The project will be a simple banking service.
- The project is divided into three domains: Account, Transaction, and User.
- Each domain projects must have the same folder structure and must be created a domain object for each service with the implementation, with the CRUD endpoints using RESTful best practices.

Resources:

- Microservices: https://spring.io/microservices
- Microservice Patterns: https://microservices.io/patterns/index.html
- Cloud: https://spring.io/cloud
- Saga: https://microservices.io/patterns/data/saga.html
- Transactional outbox: https://microservices.io/patterns/data/transactional-outbox.html
- https://spring.io/guides/tutorials/rest
- https://spring.io/guides/gs/rest-service
- https://spring.io/microservices
- https://spring.io/projects/spring-modulith

## Outcome

- Review all requirements one by one, non-functional requirements should be built accordingly to functional requirements.
- Make sure all the required dependencies are implemented for each project.
- Make sure all the best practices are applied correctly for each project.
- After creating the entire project structure, export it as a ZIP file.




    banking-microservices/
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
    │   │   │   │           └── account/
    │   │   │   │               ├── AccountServiceApplication.java
    │   │   │   │               ├── controller/
    │   │   │   │               │   └── AccountController.java
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
    │   │                   └── account/
    │   │                       └── AccountServiceTests.java
    │   └── pom.xml
    ├── transaction-service/
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── java/
    │   │   │   │   └── com/
    │   │   │   │       └── openbank/
    │   │   │   │           └── transaction/
    │   │   │   │               ├── TransactionServiceApplication.java
    │   │   │   │               ├── controller/
    │   │   │   │               │   └── TransactionController.java
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
    │   │   │   │           └── user/
    │   │   │   │               ├── UserServiceApplication.java
    │   │   │   │               ├── controller/
    │   │   │   │               │   └── UserController.java
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
    └── parent-pom.xml (Optional parent POM)

# Usage of the project packages

    <repositories>
      <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/mstfcck/openbank</url>
      </repository>
    </repositories>

    <dependency>
      <groupId>com.openbank</groupId>
      <artifactId>user-service</artifactId>
      <version>0.0.1</version>
    </dependency>