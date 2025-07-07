# ---
applyTo: '**'
---

# Account Service Instructions

- Ensure that the account service is designed to handle account-related operations such as creating, updating, and retrieving account information.
- Ensure that the account service is modular, with a clear separation of concerns between controllers, services, repositories, and models.
- Ensure that the account service is designed for easy integration with other microservices, such as user service and transaction service.

- Implement a RESTful API for the account service, allowing clients to interact with account data.
- Implement unit tests for the account service to ensure that the functionality is working as expected.
- Implement security measures to protect sensitive account information, such as using Spring Security for authentication and authorization.
- Ensure that the account service is designed to be stateless, allowing it to scale horizontally without issues.
- Use Spring Boot's built-in features for configuration management, allowing the account service to be easily configured for different environments (development, testing, production).
- Implement a database schema for the account service, ensuring that it can store account information efficiently.
- Use a relational database (e.g., PostgreSQL, MySQL) for the account service, ensuring that it can handle complex queries and relationships between entities.
- Implement database migrations using tools like Flyway or Liquibase to manage schema changes over time.
- Ensure that the account service is designed to handle high availability and fault tolerance, allowing it to recover gracefully from failures.
- Implement caching mechanisms (e.g., using Redis) to improve performance for frequently accessed account data.
- Ensure that the account service is designed to be easily testable, with clear separation of concerns and well-defined interfaces that allow for easy mocking and testing.



- Use Spring Boot to create the account service, ensuring that it follows best practices for microservice architecture.
- Implement proper error handling and validation for account operations.
- Use DTOs (Data Transfer Objects) to encapsulate account data and ensure that the API responses are well-structured and easy to understand.
- Use Spring Data JPA for database interactions, ensuring that the account service can easily perform CRUD operations on account data.
- Ensure that the account service is designed for scalability, allowing it to handle a large number of requests efficiently.
- Document the account service API using OpenAPI/Swagger, providing clear and comprehensive documentation for developers.
- Ensure that the account service is configured to run in different environments (development, testing, production) with appropriate configuration files.
- Use logging to capture important events and errors in the account service, ensuring that it is easy to monitor and debug.
- Ensure that the account service is versioned properly, allowing for backward compatibility and smooth upgrades in the future.
- Follow the project structure guidelines provided in the main documentation to maintain consistency across the microservices.
- Ensure that the account service is deployed in a containerized environment (e.g., Docker) to facilitate easy deployment and scaling.
- Implement health checks for the account service to ensure that it is running correctly and can be monitored effectively.
- Use a message broker (e.g., RabbitMQ, Kafka) for asynchronous communication with other microservices if needed, ensuring that the account service can handle events and notifications efficiently.
- Ensure that the account service is designed to handle high availability and fault tolerance, allowing it to recover gracefully from failures.
- Implement caching mechanisms (e.g., using Redis) to improve performance for frequently accessed account data.
- Ensure that the account service is designed to be stateless, allowing it to scale horizontally without issues.
- Use environment variables or configuration management tools (e.g., Spring Cloud Config) to manage sensitive information such as database credentials and API keys securely.
- Ensure that the account service is compliant with relevant regulations and standards, such as GDPR for data protection and privacy.
- Regularly review and update the account service to incorporate new features, improvements, and security patches.
- Ensure that the account service is designed to be easily extensible, allowing for future enhancements without significant refactoring.
- Collaborate with other teams to ensure that the account service integrates smoothly with other microservices in the OpenBank ecosystem.
- Conduct code reviews and ensure that the code adheres to the project's coding standards and best practices.
- Use version control (e.g., Git) to manage changes to the account service codebase, ensuring that all changes are tracked and documented.
- Ensure that the account service is tested thoroughly, including unit tests, integration tests, and end-to-end tests, to ensure that it meets the functional and non-functional requirements.
- Ensure that the account service is designed to handle high traffic loads, with appropriate performance optimizations in place.
- Implement monitoring and alerting for the account service to detect issues proactively and ensure high availability.
- Ensure that the account service is designed to be resilient, with appropriate fallback mechanisms in place for handling failures gracefully.
- Regularly update the dependencies of the account service to ensure that it is using the latest versions of libraries and frameworks, addressing security vulnerabilities and performance improvements.
- Ensure that the account service is designed to be user-friendly, with clear error messages and responses that help developers understand issues quickly.
- Ensure that the account service is designed to be easily deployable, with scripts or tools that automate the deployment process.
- Ensure that the account service is designed to be easily maintainable, with clear and concise code that is easy to read and understand.
- Ensure that the account service is designed to be easily configurable, allowing for changes to be made without requiring code changes or redeployment.
- Ensure that the account service is designed to be easily testable, with clear separation of concerns and well-defined interfaces that allow for easy mocking and testing.
- Ensure that the account service is designed to be easily debuggable, with appropriate logging and error handling that allows developers to trace issues quickly.
- Ensure that the account service is designed to be easily extensible, with clear guidelines for adding new features and functionality without breaking existing code.
- Ensure that the account service is designed to be easily documentable, with clear and comprehensive documentation that helps developers understand how to use and integrate the service.
- Ensure that the account service is designed to be easily deployable in different environments, with appropriate configuration files and scripts that allow for easy deployment and management.
- Ensure that the account service is designed to be easily maintainable, with clear coding standards and best practices that help developers write clean and maintainable code.
- Ensure that the account service is designed to be easily scalable, with appropriate load balancing and clustering mechanisms that allow it to handle increased traffic and load.
- Ensure that the account service is designed to be easily monitored, with appropriate metrics and logging that allow developers to track performance and detect issues proactively.
- Ensure that the account service is designed to be easily secured, with appropriate authentication and authorization mechanisms that protect sensitive account information and ensure that only authorized users can access the service.
- Ensure that the account service is designed to be easily integrated with other microservices, with clear APIs and interfaces that allow for seamless communication and data exchange.
- Ensure that the account service is designed to be easily versioned, with appropriate versioning strategies that allow for backward compatibility and smooth upgrades in the future.
- Ensure that the account service is designed to be easily tested, with appropriate testing frameworks and tools that allow for comprehensive testing of the service's functionality and performance.
- Ensure that the account service is designed to be easily documented, with clear and comprehensive documentation that helps developers understand how to use and integrate the service.
- Ensure that the account service is designed to be easily configured, with appropriate configuration management tools and practices that allow for easy management of configuration settings and parameters.
- Ensure that the account service is designed to be easily managed, with appropriate management tools and practices that allow for easy monitoring, logging, and troubleshooting of the service.
- Ensure that the account service is designed to be easily deployed, with appropriate deployment tools and practices that allow for easy deployment and management of the service in different environments.
- Ensure that the account service is designed to be easily maintained, with appropriate maintenance tools and practices that allow for easy updates, bug fixes, and enhancements to the service.
- Ensure that the account service is designed to be easily supported, with appropriate support tools and practices that allow for easy troubleshooting and resolution of issues
- Ensure that the account service is designed to be easily upgraded, with appropriate upgrade tools and practices that allow for easy migration to new versions of the service without disrupting existing functionality.
- Ensure that the account service is designed to be easily monitored, with appropriate monitoring tools and practices that allow for real-time tracking of the service's performance, availability, and health.
- Ensure that the account service is designed to be easily logged, with appropriate logging tools and practices that allow for detailed tracking of events, errors, and performance metrics within the service.
- Ensure that the account service is designed to be easily audited, with appropriate auditing tools and practices that allow for tracking of changes, access, and operations performed on the service.
- Ensure that the account service is designed to be easily compliant with relevant regulations and standards, such as GDPR for data protection and privacy, ensuring that it meets legal and industry requirements.
- Ensure that the account service is designed to be easily extensible, with clear guidelines and best practices for adding new features and functionality without breaking existing code or introducing technical debt.
- Ensure that the account service is designed to be easily customizable, allowing for configuration changes that can adapt the service to different business needs
- Ensure that the account service is designed to be easily deployable in cloud environments, with appropriate cloud-native practices and tools that allow for easy deployment, scaling, and management in cloud platforms such as AWS, Azure, or Google Cloud.
- Ensure that the account service is designed to be easily integrated with CI/CD pipelines, allowing for automated testing, deployment, and monitoring of the service throughout its lifecycle.
- Ensure that the account service is designed to be easily retried in case of failures, with appropriate retry mechanisms and error handling that allow for graceful recovery from transient errors and failures.