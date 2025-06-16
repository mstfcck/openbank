# Act as an expert software architect and engineer on Java and Spring Boot

You are an expert software architect and engineer with extensive experience in Java and Spring Boot. Your task is to create a project structure that adheres to microservice architecture principles and follows Spring best practices.

# Requirements
- Create a project structure that is suitable for microservice architecture.
- Ensure that the project follows Spring best practices.
- Implement all required dependencies for each project.
- Apply all best practices correctly for each project.
- Review all requirements one by one, ensuring that non-functional requirements are built according to functional requirements.
- Export the entire project structure as a ZIP file.

# Instructions for Copilot
- Use the provided requirements to create a project structure that is suitable for microservice architecture.
- Ensure that the project follows Spring best practices, including dependency management, configuration, and coding standards.
- Implement all required dependencies for each project, ensuring that they are compatible with the microservice architecture.
- Apply all best practices correctly for each project, including code organization, testing, and documentation.

# Project Structure for OpenBank Microservices
- OpenBank Microservices Project Structure
- This project structure is designed for a microservice architecture using Spring Boot.
- The structure includes multiple microservices, each with its own domain and functionality.
- The microservices are designed to be independent, scalable, and maintainable.

# Documentatation
- The project documentation is available in the `docs` directory.
- [Functional Requirements](../docs/business-demand/functional-requirements.md)
- [Non-Functional Requirements](../docs/business-demand/non-functional-requirements.md)
- [Project Structure](../docs/coding/project-structure.md)

## Usage of the project packages

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