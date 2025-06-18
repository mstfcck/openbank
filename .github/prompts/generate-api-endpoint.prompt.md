---
mode: agent
description: Generate a Spring Boot REST API endpoint following best practices (Richardson Maturity Model Level 2+)
---

# Task

Generate a Java Spring Boot REST API endpoint for the specified resource and operation. Follow these requirements:

## Requirements

### Best Practices

- Follow RESTful API best practices and the Richardson Maturity Model (at least Level 2: use HTTP verbs and resource URIs).
- Ensure the endpoint is stateless and idempotent where appropriate.
- Use standard HTTP methods and status codes.
- Use clear, resource-oriented URIs.
- Use standard HTTP methods (GET, POST, PUT, DELETE, PATCH) and appropriate status codes.
- Use meaningful names for classes, methods, and variables.
- Apply SOLID principles to the design.
- Apply separation of concerns for all layers: controller, service, repository, model, and DTO.
- Use dependency injection for all layers.
- Use constructor injection for dependencies.
- Use DTOs for request and response payloads.
- Add basic validation.
- Use JavaDoc comments to document public classes and methods.
- Return meaningful error responses.

### Technical Specifications

- Use Spring Boot 3.x and Java 17+ (or project version if specified).
- Use Spring Data JPA for repository layer.
- Use Spring Web for the controller layer.
- Use Spring Boot Starter Validation for input validation.
- Use OpenAPI (Swagger) for API documentation.
- Use Jakarta Bean Validation for input validation.
- Use Hibernate Validator for entity validation.
- Use Lombok annotations for POJOs (e.g., `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`).
- Use `@RestController` for the controller class.
- Use `@RequestMapping` for the controller class to define the base URI.
- Use clear, resource-oriented URIs (e.g., `/api/resources/{id}`).
- Use `@ControllerAdvice` for global exception handling if applicable and return meaningful error responses using `@ControllerAdvice` if possible.
- Use `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, and `@PatchMapping` for HTTP methods in the controller.
- Use `@RequestBody` for request payloads in POST/PUT methods.
- Use `@PathVariable` for path parameters.
- Use `@RequestParam` for query parameters.
- Use `@Valid` for validating request payloads.
- Use `@ResponseStatus` for setting HTTP status codes in responses.
- Use `@ResponseBody` for returning JSON responses.
- Use `@CrossOrigin` for enabling CORS if necessary.
- Use `@Service` for service classes.
- Use `@Entity` for JPA entities.
- Use `@Repository` for repository interfaces.
- Use `@Transactional` for service methods that modify data.
- Use `@Component` for any other Spring components.
- Use `@Configuration` for configuration classes.
- Use H2 in-memory database for persistence (if persistence is required).

- Implement the controller class with the specified resource and operation if it is not already implemented.
- Implement the service interface and class if not already implemented.
- Implement the repository interface if not already implemented.
- Implement request/response DTOs for input/output if not already implemented.

- Do not include business logic in the controller; delegate to the service layer.

- Include sample request/response payloads as code comments.

- Return meaningful error responses using `@ControllerAdvice` if possible.

## Input

- Provide the resource and the operation to be performed.
- Always ask for the resource and operation to be performed.

### Input Format
- Resource: {{resource}}
- Operation: {{operation}}

## Output

- Implement the api endpoint with the specified resource and operation.
- Ensure the code is well-structured and follows the requirements.
- Include necessary imports.
- Do not include unrelated code or explanations.
- Build the code and ensure it compiles without errors.

---

You can reference additional project instructions or requirements as needed.
