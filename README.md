# Blog API

REST API server for a blog platform. Users sign up via Google OAuth, write posts, and leave comments. Built with Spring Boot, deployed to AWS with Docker and GitHub Actions CI/CD.

## Tech Stack

**Backend:** Java 17, Spring Boot, Spring Security, JWT, Spring Data JPA, QueryDSL

**Database:** MySQL 8, Redis (caching)

**Infrastructure:** Docker Compose, GitHub Actions, AWS (EC2, ECR, RDS)

**Testing:** JUnit 5, Gradle

## Architecture

### System Overview

![system-architecture](https://user-images.githubusercontent.com/67671991/205954016-a028bdbc-f0a6-4b24-b84f-064449cff989.png)

### Hexagonal Architecture (Domain Layer)

![hexagonal-architecture](https://user-images.githubusercontent.com/67671991/204727244-7aa8c148-5265-4945-888b-c58015c71be7.png)

## Features

**Authentication**
- Google OAuth2 social login
- JWT-based session management (access + refresh tokens)
- Spring Security integration

**Posts & Comments**
- CRUD operations with author-only edit/delete
- Pagination with QueryDSL dynamic queries
- Comment threads on posts

**User Management**
- Profile view and update
- Account deletion

## Deployment

CI/CD pipeline runs on every push to `develop`:

1. **Build** — Gradle build + unit tests on GitHub Actions
2. **Package** — Jib builds Docker image, pushes to AWS ECR
3. **Deploy** — Self-hosted runner pulls image and runs on EC2

See [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml) for the full pipeline.

## Local Development

```bash
# Start MySQL + Redis
docker compose -f docker/docker-compose.yml up -d

# Run the app
./gradlew bootRun
```

## Project Structure

```
src/main/java/me/jaeyeop/blog/
├── post/               # Post domain (hexagonal architecture)
│   ├── adapter/        # REST controllers, JPA repositories
│   ├── application/    # Use cases, service layer
│   └── domain/         # Entities, value objects
├── comment/            # Comment domain (same structure)
├── user/               # User management
└── commons/            # Shared config, auth, error handling
    ├── authentication/ # OAuth2 + JWT
    ├── token/          # Token management
    ├── config/         # Spring configuration
    └── error/          # Global exception handling
```
