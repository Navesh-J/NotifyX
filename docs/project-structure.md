### 📚 Documentation

- [README](../README.md)
- [Architecture](architecture.md)
- [Design Patterns](design-patterns.md)
- [API Guide](api-guide.md)
- [Docker Guide](docker.md)
- [Testing](testing.md)
- **Project Structure**

# 📂 Project Structure

## Overview

NotifyX follows a feature-oriented layered architecture designed to demonstrate enterprise Spring Boot development practices.

The project is organized to achieve:

- Clear separation of concerns
- High cohesion
- Loose coupling
- Easy maintainability
- Easy extensibility
- Production-inspired organization

Instead of placing all classes into a few large packages, every package has a clearly defined responsibility.

---

# Project Layout

```
notifyx
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.navesh.notifyx
│   │   │
│   │   ├── aspect
│   │   ├── audit
│   │   ├── config
│   │   ├── controller
│   │   ├── core
│   │   ├── dto
│   │   ├── exception
│   │   ├── factory
│   │   ├── gateway
│   │   ├── impl
│   │   │     ├── email
│   │   │     ├── sms
│   │   │     └── push
│   │   ├── model
│   │   ├── scheduler
│   │   └── service
│   │
│   └── resources
│
├── src/test
│   ├── unit
│   └── integration
│
├── docs
│
├── Dockerfile
├── compose.yaml
├── README.md
└── pom.xml
```

---

# Package Responsibilities

## aspect/

Contains Aspect-Oriented Programming components.

Responsibilities:

- Logging
- Method interception
- Cross-cutting concerns

Examples

```
NotificationLoggingAspect
```

---

## audit/

Responsible for notification history.

Contains:

- Audit Entity
- Repository
- Audit Service

Responsibilities:

- Store delivery history
- Persist notification metadata
- Audit reporting

---

## config/

Contains Spring configuration.

Examples

```
NotificationConfig

RetryConfig

AppProperties
```

Responsibilities:

- Bean configuration
- Configuration properties
- Environment setup

---

## controller/

Contains REST controllers.

Responsibilities:

- HTTP endpoints
- Request validation
- Response generation

Controllers never contain business logic.

---

## core/

The heart of NotifyX.

Contains shared abstractions.

Examples

```
NotificationService

NotificationChannel

NotificationRequest

NotificationResponse
```

Every notification provider depends on this package.

---

## dto/

Contains Data Transfer Objects.

Purpose:

Transfer data between layers without exposing internal models.

Typical contents:

- Request DTOs
- Response DTOs
- Error Responses

---

## exception/

Contains centralized exception handling.

Examples

```
GlobalExceptionHandler

NotificationException

ProviderNotFoundException
```

Responsibilities:

- Error mapping
- Consistent API responses
- Exception hierarchy

---

## factory/

Contains provider selection logic.

Main class

```
NotificationServiceFactory
```

Responsibilities:

- Locate provider
- Return matching implementation
- Hide provider-selection logic

---

## gateway/

Contains integrations with external systems.

Examples

```
JavaMailSender

SmsGateway

PushGateway
```

Responsibilities:

- Third-party communication
- External provider abstraction

---

## impl/

Contains notification provider implementations.

Structure

```
impl

├── email

├── sms

└── push
```

Every implementation provides the same interface:

```
NotificationService
```

This demonstrates the Strategy Pattern.

---

## model/

Contains domain models.

Examples

- Entities
- Value Objects
- Domain Models

Models represent business concepts.

---

## scheduler/

Contains scheduled jobs.

Examples

```
RetryScheduler
```

Responsibilities:

- Retry failed notifications
- Background processing
- Scheduled tasks

---

## service/

Contains business logic.

Responsibilities:

- Notification orchestration
- Retry coordination
- Audit coordination
- Business rules

The service layer coordinates the application.

---

# Resource Directory

```
src/main/resources
```

Contains:

- application.yml
- profile configurations
- static resources
- templates (if required)

Spring Boot automatically loads these resources.

---

# Test Structure

```
src/test
```

is divided into

```
unit
```

and

```
integration
```

This separation keeps tests organized according to scope.

---

# Documentation Directory

```
docs/
```

Contains project documentation.

```
architecture.md

design-patterns.md

api-guide.md

docker.md

testing.md

project-structure.md
```

Keeping documentation separate from the README makes the repository easier to navigate.

---

# Root Files

## README.md

Project overview.

---

## Dockerfile

Defines the application image.

---

## compose.yaml

Starts multiple containers.

---

## pom.xml

Maven build configuration.

---

# Dependency Flow

A request typically travels through the following packages.

```
controller

↓

service

↓

factory

↓

impl

↓

gateway

↓

audit

↓

repository
```

Each package has a single responsibility.

---

# Design Principles

The package structure reflects several software engineering principles.

## High Cohesion

Classes with similar responsibilities are grouped together.

---

## Loose Coupling

Packages communicate through interfaces rather than concrete implementations.

---

## Separation of Concerns

Each package focuses on one specific responsibility.

---

## Extensibility

Adding a new notification provider requires changes only within the implementation package and minimal configuration.

---

# Why This Structure?

This organization was chosen to resemble real-world Spring Boot applications rather than simple tutorial projects.

Benefits include:

- Easier navigation
- Better scalability
- Cleaner architecture
- Simpler testing
- Lower coupling
- Better maintainability

As the application grows, new features can be introduced with minimal impact on existing code.

---

# Summary

NotifyX uses a modular package structure where each package has a clearly defined responsibility.

This organization complements Spring Boot's Dependency Injection model, encourages clean architecture, and makes the project easier to understand, extend, and maintain.