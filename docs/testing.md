### 📚 Documentation

- [README](../README.md)
- [Architecture](architecture.md)
- [Design Patterns](design-patterns.md)
- [API Guide](api-guide.md)
- [Docker Guide](docker.md)
- **Testing**
- [Project Structure](project-structure.md)

# 🧪 Testing Guide

## Overview

Testing is a fundamental part of NotifyX.

Rather than relying only on manual API testing, the project includes multiple layers of automated testing to verify the application from individual components to complete request flows.

The testing strategy follows the well-known **Testing Pyramid**, where fast, isolated unit tests form the foundation, followed by integration tests that verify component interaction.

```
                 End-to-End Tests
                       ▲
                       │
              Integration Tests
                       ▲
                       │
                 Unit Tests
```

This approach provides confidence that individual classes work correctly while ensuring the entire application functions as expected.

---

# Testing Philosophy

NotifyX follows four simple principles.

- Test business logic independently.
- Test component interaction.
- Test complete application flow.
- Keep tests readable and maintainable.

The goal is not simply to increase code coverage, but to verify real application behavior.

---

# Testing Stack

| Tool | Purpose |
|-------|---------|
| JUnit 5 | Test framework |
| Mockito | Mocking dependencies |
| Spring Boot Test | Integration testing |
| MockMvc | Controller testing |
| H2 Database | Persistence testing |
| Maven Surefire | Test execution |

---

# Test Structure

```
src
└── test
    └── java
        └── com.navesh.notifyx
            ├── unit
            └── integration
```

Tests are separated according to their responsibility.

---

# Unit Testing

## Purpose

Unit tests verify the behavior of a single class in complete isolation.

Dependencies are mocked so that only the class under test is evaluated.

```
Class Under Test

↓

Mock Dependencies

↓

Assertions
```

Unit tests should be:

- Fast
- Independent
- Repeatable
- Deterministic

---

# Unit Tests in NotifyX

Current unit test suite includes:

```
CompositeNotificationServiceTest

EmailNotificationServiceTest

NotificationApplicationServiceTest

NotificationRetryServiceTest

NotificationServiceFactoryTest

PushNotificationServiceTest

SmsNotificationServiceTest
```

Each test focuses on one specific component.

---

## Example

Testing the factory:

```
NotificationServiceFactory

↓

EMAIL

↓

Returns EmailNotificationService
```

The test verifies provider selection logic without involving controllers or HTTP requests.

---

Another example:

```
NotificationApplicationService

↓

Mock Factory

↓

Mock Provider

↓

Verify orchestration
```

Only orchestration logic is tested.

---

# Integration Testing

## Purpose

Integration tests verify how multiple Spring components work together.

Unlike unit tests, Spring starts the application context.

```
Controller

↓

Service

↓

Repository

↓

Database
```

The interaction between these components is validated.

---

# Integration Tests in NotifyX

```
NotificationControllerIT

AuditRepositoryIT

NotificationEndToEndIT

RetrySchedulerIT
```

These tests verify the application's real behavior.

---

## Controller Integration

Instead of calling service methods directly,

HTTP requests are executed through Spring.

```
HTTP Request

↓

Controller

↓

Service

↓

Response
```

This verifies:

- Request mapping
- Validation
- JSON serialization
- HTTP status codes
- Exception handling

---

# Repository Integration

```
Entity

↓

Repository

↓

H2 Database

↓

Assertions
```

Repository tests ensure persistence behaves correctly.

---

# End-to-End Flow

One notification request travels through the complete application.

```
Client

↓

Controller

↓

Application Service

↓

Factory

↓

Provider

↓

Audit

↓

Database
```

This confirms that all layers work together.

---

# Mocking

Mockito is used heavily in unit testing.

Instead of sending real emails,

```
Email Service

↓

Mock JavaMailSender
```

Instead of real SMS providers,

```
Mock SmsGateway
```

Instead of real push providers,

```
Mock PushGateway
```

Benefits:

- Faster tests
- No external dependencies
- Predictable results
- Easy failure simulation

---

# What Is Not Mocked?

Integration tests intentionally avoid mocking core Spring infrastructure.

Instead they verify:

- Spring Dependency Injection
- Bean creation
- Repository interaction
- Transaction behavior
- Validation
- Exception handling

---

# Assertions

A good test should verify observable behavior.

Examples include:

- Correct notification provider selected
- Notification successfully delivered
- Audit record persisted
- Retry executed
- Expected HTTP status returned
- Correct response body generated

Avoid testing implementation details whenever possible.

---

# Test Naming

NotifyX follows descriptive naming.

Examples:

```
EmailNotificationServiceTest

NotificationServiceFactoryTest

NotificationControllerIT
```

The class name immediately communicates:

- What is being tested
- The scope of the test

---

# Running Tests

Run the complete suite

```bash
./mvnw clean test
```

Run verification

```bash
./mvnw clean verify
```

Run a single test

```bash
./mvnw -Dtest=NotificationServiceFactoryTest test
```

---

# Continuous Testing

During development, tests should be executed frequently.

Typical workflow:

```
Implement Feature

↓

Run Unit Tests

↓

Run Integration Tests

↓

Refactor

↓

Run Tests Again

↓

Commit
```

This helps catch regressions early.

---

# Why Both Unit and Integration Tests?

A project with only unit tests may pass while failing when Spring wires the application together.

A project with only integration tests becomes slow and difficult to debug.

NotifyX combines both approaches.

```
Unit Tests

↓

Business Logic

──────────────

Integration Tests

↓

Component Interaction
```

The two complement one another.

---

# Testing Best Practices Used

✔ Small focused tests

✔ Independent execution

✔ Clear naming

✔ Mock external systems

✔ Verify behavior instead of implementation

✔ Test positive and negative scenarios

✔ Separate unit and integration tests

✔ Keep production code free of testing logic

---

# Current Coverage

The project includes tests for:

- Notification provider selection
- Notification orchestration
- Email provider
- SMS provider
- Push provider
- Composite notifications
- Retry service
- Controller endpoints
- Repository persistence
- End-to-end notification flow

This provides confidence across the application's most important components.

---

# Future Testing Improvements

Potential enhancements include:

- Testcontainers for PostgreSQL integration
- Docker Compose integration tests
- Performance testing
- Load testing
- Contract testing
- Mutation testing
- Code coverage reporting with JaCoCo
- GitHub Actions test automation

---

# Why Testing Matters

Testing is not simply about finding bugs.

A strong test suite enables developers to:

- Refactor confidently
- Add new features safely
- Detect regressions early
- Improve code quality
- Document expected behavior

Well-designed tests also serve as executable documentation for future contributors.

---

# Summary

NotifyX demonstrates a layered testing strategy built around unit and integration testing.

By combining JUnit 5, Mockito, Spring Boot Test, and MockMvc, the project validates both isolated business logic and complete application workflows.

The result is a reliable, maintainable codebase where new features can be added with confidence while preserving existing functionality.