### 📚 Documentation

- **README**
- [Architecture](docs/architecture.md)
- [Design Patterns](docs/design-patterns.md)
- [API Guide](docs/api-guide.md)
- [Docker Guide](docs/docker.md)
- [Testing](docs/testing.md)
- [Project Structure](docs/project-structure.md)

# NotifyX

> **A production-inspired Spring Boot notification platform showcasing modern Dependency Injection, Strategy, Factory, Composite, AOP, validation, scheduling, testing, and Docker through a modular multi-channel messaging architecture.**

NotifyX demonstrates how to build a clean, extensible notification system in Spring Boot using real-world design patterns and a layered architecture.

It supports multiple delivery channels:

- Email
- SMS
- Push Notifications
- Broadcast delivery through all available channels

The project is intentionally built as a **Dependency Injection showcase**. Each part of the system is wired through Spring, making it easy to swap implementations without changing the core application flow.

---

## Table of Contents

- [Overview](#overview)
- [Why NotifyX Exists](#why-notifyx-exists)
- [Features](#features)
- [Architecture](#architecture)
- [Dependency Injection Showcase](#dependency-injection-showcase)
- [Design Patterns Used](#design-patterns-used)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Running Locally](#running-locally)
- [Running with Docker](#running-with-docker)
- [Testing](#testing)
- [Documentation & Swagger](#documentation--swagger)
- [Learning Outcomes](#learning-outcomes)
- [Future Enhancements](#future-enhancements)
- [Author](#author)

---

## Overview

NotifyX is a modular notification platform built with Spring Boot and Java 21.

It demonstrates:

- clean layered architecture
- interface-driven design
- strategy-based provider selection
- factory-based resolution
- composite/broadcast notifications
- JPA-based audit logging
- retry-based recovery
- scheduled retries
- AOP-based logging
- validation and global exception handling
- API documentation with OpenAPI/Swagger
- unit, integration, and end-to-end tests
- Dockerized development workflow

The app is designed to be easy to understand, easy to extend, and easy to discuss in interviews.

---

## Why NotifyX Exists

Most real applications need more than one notification channel.

A user might receive:

- a confirmation email
- an SMS alert
- a push notification
- or all of them together

NotifyX was built to simulate that real-world problem while showcasing how Spring Boot handles:

- runtime implementation switching
- dependency injection
- separation of concerns
- reusable business rules
- infrastructure concerns like retries, logging, and auditing

---

## Features

- Multi-channel notification support
- Email notifications via Spring Mail and MailHog
- SMS notifications via a gateway abstraction
- Push notifications via a gateway abstraction
- Broadcast notifications across all providers
- Spring-based factory for selecting the correct provider
- AOP logging for method entry/exit and execution timing
- Audit logging for notification attempts
- Retry support for failed deliveries
- Scheduled retry processing
- Request validation with Jakarta Bean Validation
- Global exception handling with consistent error responses
- OpenAPI / Swagger UI documentation
- Full unit test suite
- Integration and end-to-end tests
- Dockerfile and Docker Compose support
- Clean, layered package structure

---

## Architecture

NotifyX follows a layered Spring Boot architecture:

```text
Client
  ↓
NotificationController
  ↓
NotificationApplicationService
  ↓
NotificationServiceFactory
  ↓
NotificationService implementations
  ├── EmailNotificationService
  ├── SmsNotificationService
  └── PushNotificationService
  ↓
Provider-specific gateways
  ├── JavaMailSender
  ├── SmsGateway
  └── PushGateway
  ↓
AuditService
  ↓
AuditRepository
  ↓
Database
```
## Dependency Injection Showcase

NotifyX was built to demonstrate multiple Dependency Injection (DI) styles in a practical Spring Boot application.

### Constructor Injection

Constructor injection is used throughout the project for required dependencies, promoting immutability, easier testing, and explicit dependency management.

**Examples:**

- `NotificationController`
- `NotificationApplicationService`
- `NotificationRetryService`
- `CompositeNotificationService`

### Collection Injection

Spring automatically injects every `NotificationService` implementation into `CompositeNotificationService`.

This allows the application to broadcast notifications across all available channels without hardcoding provider implementations.

### Field Injection

Field injection is used selectively for injecting configuration properties where constructor injection is unnecessary.

### Interface-Based Design

Every notification provider implements the `NotificationService` interface, allowing the application to switch implementations at runtime based on the requested notification channel.

---

## Design Patterns Used

### 1. Strategy Pattern

Each notification provider is implemented as a strategy by implementing the `NotificationService` interface.

**Concrete strategies:**

- `EmailNotificationService`
- `SmsNotificationService`
- `PushNotificationService`

### 2. Factory Pattern

`NotificationServiceFactory` selects the appropriate notification strategy based on the requested notification channel, keeping business logic independent of provider implementations.

### 3. Composite Pattern

`CompositeNotificationService` maintains a collection of all notification providers and broadcasts a notification request to every supported channel.

### 4. Gateway Pattern

External integrations such as SMS and Push notifications are encapsulated behind gateway interfaces, allowing implementations to change without affecting higher application layers.

### 5. Repository Pattern

`AuditRepository` abstracts persistence operations for notification audit logs using Spring Data JPA.

### 6. Aspect-Oriented Programming (AOP)

`NotificationLoggingAspect` centralizes logging and execution timing, keeping cross-cutting concerns separate from business logic.

### 7. Retry Pattern

`NotificationRetryService`, together with Spring Retry and scheduled tasks, automatically retries failed notification delivery attempts.

---

## Tech Stack

### Core

- Java 21
- Spring Boot 3

### Spring Modules

- Spring Web
- Spring Validation
- Spring Data JPA
- Spring Mail
- Spring AOP
- Spring Retry
- Spring Scheduling

### Documentation

- OpenAPI / Swagger UI (`springdoc-openapi`)

### Database

- H2 Database

### Testing

- JUnit 5
- Mockito
- MockMvc

### Development Tools

- Lombok
- Docker
- Docker Compose
- MailHog

## Project Structure

```text
com.navesh.notifyx
├── aspect
│   └── NotificationLoggingAspect.java
├── audit
│   ├── AuditRepository.java
│   ├── AuditService.java
│   ├── AuditServiceImpl.java
│   └── NotificationAuditLog.java
├── config
│   ├── OpenApiConfiguration.java
│   ├── ProviderProperties.java
│   ├── RetryConfiguration.java
│   ├── RetryProperties.java
│   └── SchedulingConfiguration.java
├── controller
│   └── NotificationController.java
├── core
│   ├── AuditStatus.java
│   ├── NotificationChannel.java
│   ├── NotificationService.java
│   └── NotificationStatus.java
├── dto
│   ├── BroadcastNotificationRequest.java
│   ├── BroadcastNotificationResponse.java
│   ├── ChannelResult.java
│   ├── NotificationRequest.java
│   └── NotificationResponse.java
├── exception
│   ├── ChannelUnavailableException.java
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── NotificationDeliveryException.java
│   └── NotificationException.java
├── factory
│   └── NotificationServiceFactory.java
├── gateway
│   ├── MockPushGateway.java
│   ├── MockSmsGateway.java
│   ├── PushGateway.java
│   └── SmsGateway.java
├── impl
│   ├── CompositeNotificationService.java
│   ├── email
│   │   └── EmailNotificationService.java
│   ├── push
│   │   └── PushNotificationService.java
│   └── sms
│       └── SmsNotificationService.java
├── model
│   ├── EmailPayload.java
│   ├── PushPayload.java
│   └── SmsPayload.java
├── scheduler
│   └── NotificationScheduler.java
└── service
    ├── NotificationApplicationService.java
    └── NotificationRetryService.java
```

---

## API Endpoints

### Send a Notification

**POST** `/api/notifications/send`

Sends a notification through the selected delivery channel.

#### Example Request

```json
{
  "channel": "EMAIL",
  "recipient": "john@example.com",
  "message": "Hello from NotifyX"
}
```

---

### Broadcast a Notification

**POST** `/api/notifications/send-all`

Sends the notification through every available notification channel.

#### Example Request

```json
{
  "recipient": "john@example.com",
  "message": "Broadcast message from NotifyX"
}
```

---

## Documentation & Swagger

The API is documented using **OpenAPI** and **Swagger UI**.

### Available Endpoints

- `/swagger-ui`
- `/api-docs`

---

## Configuration

NotifyX uses **profile-based configuration** to keep provider-specific settings separate from the core application.

### Default Configuration

```text
src/main/resources/application.yml
```

### Provider Profiles

```text
application-email.yml
application-sms.yml
application-push.yml
```

Profiles allow notification providers to be configured independently without changing application code.

---

## Running Locally

### Prerequisites

- Java 21
- Maven
- Docker Desktop or Docker Engine
- MailHog (for local email testing)

### Start MailHog

If you're using Docker:

```bash
docker run -d \
  --name mailhog \
  -p 1025:1025 \
  -p 8025:8025 \
  mailhog/mailhog
```

### Run the Application

Using Maven:

```bash
./mvnw spring-boot:run
```

Or run the application directly from your preferred IDE.

### Access Swagger UI

Open:

```text
http://localhost:8080/swagger-ui
```

Or use the custom Swagger path configured in your application.

---

## Running with Docker

### Build the Docker Image

```bash
docker build -t notifyx:1.0 .
```

### Run the Container

```bash
docker run --name notifyx-app -p 8080:8080 notifyx:1.0
```

### Run with Docker Compose

```bash
docker compose up --build
```

### Docker Services

| Service | Purpose |
|---------|---------|
| **notifyx** | Spring Boot application |
| **mailhog** | Captures outgoing emails for local testing |

Docker Compose creates a private network, allowing services to communicate using service names instead of `localhost`.

## Testing

NotifyX includes a comprehensive test suite covering unit, integration, and end-to-end scenarios.

### Unit Tests

- `NotificationServiceFactoryTest`
- `EmailNotificationServiceTest`
- `SmsNotificationServiceTest`
- `PushNotificationServiceTest`
- `CompositeNotificationServiceTest`
- `NotificationApplicationServiceTest`
- `NotificationRetryServiceTest`

### Integration Tests

- `NotificationControllerIT`
- `NotificationEndToEndIT`
- `AuditRepositoryIT`
- `RetrySchedulerIT`

### Test Objectives

The test suite verifies:

- Provider selection through the factory
- Request and response handling
- Audit log persistence
- Retry behavior for failed notifications
- Controller validation and exception handling
- End-to-end application wiring

### Run the Test Suite

```bash
./mvnw clean verify
```

---

## Documentation & Swagger

NotifyX includes **OpenAPI** documentation with an interactive **Swagger UI**.

The API documentation is generated directly from the running Spring Boot application, making it easy to explore and test the available endpoints.

### Swagger Features

- Inspect request payloads
- Test API endpoints interactively
- View response models
- Validate API behavior during development

---

## Learning Outcomes

NotifyX was built as a learning project while demonstrating production-inspired software engineering practices.

The project showcases:

- Spring IoC and Dependency Injection
- Layered Spring Boot architecture
- Interface-driven design
- Strategy, Factory, and Composite design patterns
- Gateway abstractions for external integrations
- Validation and centralized exception handling
- Aspect-Oriented Programming (AOP) for logging
- Retry mechanisms with scheduled processing
- Unit, integration, and end-to-end testing
- Docker-based containerization and local development

---

## Future Enhancements

Potential improvements include:

- PostgreSQL persistence
- Real SMS provider integration
- Real push notification provider integration
- HTML email template support
- Advanced retry policies with exponential backoff
- Health checks for external dependencies
- Metrics and monitoring
- CI/CD pipeline
- Production deployment profile
- Enhanced audit querying and history APIs

---

## Author

**Navesh**

Built as a production-inspired Spring Boot project showcasing Dependency Injection, clean architecture, design patterns, testing, and Dockerized development.