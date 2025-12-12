# AGENTS

This project is a Spring Boot-based blog service, designed with a domain-driven package structure and layered architecture for maintainability and scalability.

## Main Structure & Domains
- **authentication, comment, post, user**: Each domain is organized into adapter (external interfaces such as Controller, Repository), application (service layer), and domain (entities, business logic).
- **commons**: Provides shared functionalities such as authentication, configuration, error handling, persistence, and token management, reused across multiple domains.

## Architecture & Technology Stack
- Spring Boot with Gradle build system
- Dependency Injection: Constructor-based, private final fields
- Data Access: Spring Data JPA (prevents SQL injection via parameterized queries)
- Logging: SLF4J (use parameterized logging)
- Input Validation: JSR-380 annotations (e.g., @NotNull, @Size)
- Configuration: YAML files (application.yml, application-dev.yml, application-local.yml), Spring profiles, @ConfigurationProperties for type-safe config, environment variables for secrets
- Exception Handling: Centralized error handling, custom exceptions
- Code Organization: Feature/domain-based package structure, separation of concerns (thin controllers, focused services, simple repositories)
- Utility Classes: Final with private constructors
- Services: Stateless, testable, inject repositories via constructor, use domain IDs or DTOs for method signatures
- Testing: Unit and integration tests, test resources, Gradle build
- Maintainability: Modular design, clear boundaries between layers
- Extensibility: Designed for easy addition of new features and domains

## Environment & Configuration
- Profile-based configuration using application.yml, application-dev.yml, application-local.yml
- Secrets and sensitive data should be managed via environment variables or secret management systems

## Best Practices
- Use constructor injection for all required dependencies
- Keep controllers thin, services focused, repositories simple
- Use SLF4J for all logging, avoid System.out.println and concrete implementations
- Validate all request bodies and parameters using JSR-380 annotations and BindingResult
- Organize code by feature/domain, not by layer
- Utility classes should be final and have private constructors

## Other Notes
- Designed for maintainability, scalability, and clear separation of concerns
- Domain interactions are handled in the service layer (application)
- Common modules are leveraged for authentication, error handling, and shared logic
- Follows modern Spring Boot development guidelines for enterprise applications
