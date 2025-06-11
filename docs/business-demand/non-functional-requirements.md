# Non-Functional Requirements

## Overview

The project must be develop using Spring Boot and the relevent modules based on the requirements.

## Project Platform, Framework, Lanugage and Tools

- Platform: Web API
- Framework: Spring Boot 3.4.5
- SDK Version: OpenSDK 24
- Language: Java 24
- Builder: Maven
- Packaging: Jar

## Project Metadata

- Group: com.openbank
- Artifact: [module-name]
- Name: [module-name]
- Description: Open Bank API
- Package name: com.openbank.[module-name]

## Project Back-End

- Development Language/Framework: Java, Spring Boot
- Libraries
  - Lombok: All POJOs must be use Lombok for getter and setter.
- Database: H2
- API: RESTful API adhering to best practices (statelessness, standard HTTP methods, clear resource naming, proper status codes)
- Architecture
  - Microservice: Architect an application as a collection of independently deployable, loosely coupled services
    - Independently Deployable: https://microservices.io/post/architecture/2022/05/04/microservice-architecture-essentials-deployability.html
    - Loosely Coupled: https://microservices.io/post/architecture/2023/03/28/microservice-architecture-essentials-loose-coupling.html
- Service Boundaries
  - Decompose by business capability - define services corresponding to business capabilities: https://microservices.io/patterns/decomposition/decompose-by-business-capability.html
  - Decompose by subdomain - define services corresponding to DDD subdomains: https://microservices.io/patterns/decomposition/decompose-by-subdomain.html
  - Self-contained Service - design services to handle synchronous requests without waiting for other services to respond: https://microservices.io/patterns/decomposition/self-contained-service.html
- Service Collaboration
  - Database per Service - each service has its own private database: https://microservices.io/patterns/data/database-per-service.html
  - Shared database - services share a database: https://microservices.io/patterns/data/shared-database.html
  - Saga - use sagas, which a sequences of local transactions, to maintain data consistency across services: https://microservices.io/patterns/data/saga.html
  - Command-side replica - maintain a queryable replica of data in a service that implements a command: https://microservices.io/patterns/data/command-side-replica.html
  - API Composition - implement queries by invoking the services that own the data and performing an in-memory join: https://microservices.io/patterns/data/api-composition.html
  - CQRS - implement queries by maintaining one or more materialized views that can be efficiently queried: https://microservices.io/patterns/data/cqrs.html
  - Domain event - publish an event whenever data changes: https://microservices.io/patterns/data/domain-event.html
  - Event sourcing - persist aggregates as a sequence of events: https://microservices.io/patterns/data/event-sourcing.html

## Architecture, Pattern and Practices

- The project must be apply all SOLID principles properlty.
- The project must be apply Seperation of Concern principle.
- The project must be design based on microservice architecture. - all domains must be created as separate microservices.
- The project must be follow RESTful API best practices.
- The project must be apply Dependecy Injection.
- The project must be independently deployable.
- The project must be loosely coupled.
- The project must be designed as "database per service". - each service has its own private database
- The project must be apply "saga" - use sagas, which a sequences of local transactions, to maintain data consistency across services
- The project must be apply "CQRS" - implement queries by maintaining one or more materialized views that can be efficiently queried
- The project must be apply "Domain event" - publish an event whenever data changes
- The project must be apply "Event sourcing" - persist aggregates as a sequence of events
- The project must have a separate "Shared" project for shared integrations to be used together and must be referenced by all other microservice projects.
- The project must have an "API Gateway" for microservices.
- The project must be created by Spring best practices, and the entire structure must be built by microservice architecture.
- The project folder and files must be created for the service structures based on the best practices, such as domain, service, database, etc.
- The project must implement Dockerfile for each project, and Docker Compose for all setup based on the Dockerfiles which is must be ready for deployment.

## Resources

- Spring Cloud: https://spring.io/cloud
- Spring REST: https://spring.io/guides/tutorials/rest
- Spring Rest Service: https://spring.io/guides/gs/rest-service
- Spring Microservices: https://spring.io/microservices
- Spring Modulith: https://spring.io/projects/spring-modulith
- Spring Data: https://spring.io/projects/spring-data
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Spring Dependencies: https://docs.spring.io/spring-framework/reference/core/beans/dependencies.html
  - Dependency Injection: https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html
  - Autowiring Collaborators: https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-autowire.html
  - Method Injection: https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-method-injection.html
- Spring OpenAPI: https://springdoc.org/
- Lombok: https://projectlombok.org/features/GetterSetter
- Microservices: https://spring.io/microservices
- Microservice Patterns: https://microservices.io/patterns/index.html
- Saga: https://microservices.io/patterns/data/saga.html
- Transactional Outbox: https://microservices.io/patterns/data/transactional-outbox.html
