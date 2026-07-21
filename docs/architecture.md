### 📚 Documentation

- [README](../README.md)
- **Architecture**
- [Design Patterns](design-patterns.md)
- [API Guide](api-guide.md)
- [Docker Guide](docker.md)
- [Testing](testing.md)
- [Project Structure](project-structure.md)

# 🏛️ NotifyX Architecture

## Overview

NotifyX follows a layered architecture inspired by production-grade Spring Boot applications.

The project separates responsibilities into clearly defined layers, making the system modular, testable, and easy to extend.

```
                Client
                   │
                   ▼
      NotificationController
                   │
                   ▼
 NotificationApplicationService
                   │
                   ▼
 NotificationServiceFactory
                   │
      ┌────────────┼────────────┐
      ▼            ▼            ▼
   Email         SMS          Push
      │            │            │
      ▼            ▼            ▼
 JavaMail     SmsGateway    PushGateway
      │            │            │
      └────────────┼────────────┘
                   │
                   ▼
             Audit Service
                   │
                   ▼
           Audit Repository
                   │
                   ▼
                Database
```

---

# Request Lifecycle

A notification request travels through several layers.

## Step 1

The client sends an HTTP request.

```
POST /api/notifications/send
```

↓

## Step 2

Spring MVC forwards the request to

```
NotificationController
```

Responsibilities:

- Validate request
- Handle HTTP concerns
- Delegate business logic

The controller never talks directly to Email, SMS or Push services.

---

## Step 3

The controller delegates to

```
NotificationApplicationService
```

Responsibilities:

- Business orchestration
- Provider selection
- Retry coordination
- Audit coordination

This acts as the application's orchestration layer.

---

## Step 4

The application service requests the correct implementation from

```
NotificationServiceFactory
```

Instead of:

```
if(channel == EMAIL)
```

or

```
switch(channel)
```

the factory delegates provider resolution to Spring.

This keeps the application open for extension while remaining closed for modification.

---

## Step 5

The selected implementation executes.

Possible implementations:

```
EmailNotificationService

SmsNotificationService

PushNotificationService
```

Each implementation contains only provider-specific logic.

---

## Step 6

Provider-specific gateways communicate with external systems.

Examples:

```
JavaMailSender

SmsGateway

PushGateway
```

The application never depends directly on external vendors.

Instead it depends on abstractions.

---

## Step 7

Audit information is persisted.

```
AuditService
```

↓

```
AuditRepository
```

↓

```
NotificationAuditLog
```

This allows every notification attempt to be recorded independently from delivery success.

---

# Broadcast Flow

Broadcast notifications are handled differently.

```
Client
      │
      ▼

CompositeNotificationService

      │

────────────────────────────

Email

SMS

Push

────────────────────────────

      │

Aggregate Result
```

Spring automatically injects every available NotificationService implementation.

The composite service iterates over them and collects individual delivery results.

No implementation needs to know about another.

---

# Layer Responsibilities

## Controller Layer

Responsible for:

- HTTP endpoints
- Validation
- Response generation

Not responsible for:

- Business logic
- Provider selection
- Database operations

---

## Service Layer

Responsible for:

- Business rules
- Notification orchestration
- Retry decisions
- Audit coordination

---

## Factory Layer

Responsible for:

- Selecting the correct notification provider

No delivery logic exists inside the factory.

---

## Provider Layer

Responsible for:

- Delivering notifications

Each provider implements the same interface.

```
NotificationService
```

making them interchangeable.

---

## Gateway Layer

Responsible for communication with third-party providers.

Examples:

- SMTP
- SMS Provider
- Push Provider

Replacing a provider does not affect the rest of the application.

---

## Persistence Layer

Responsible for storing notification history.

The business layer communicates only with the service abstraction.

---

# Why This Architecture?

The architecture intentionally follows SOLID principles.

## Single Responsibility

Every class has one reason to change.

---

## Open/Closed

New providers can be added without modifying existing providers.

---

## Liskov Substitution

Every NotificationService implementation can replace another.

---

## Interface Segregation

Clients depend only on the operations they require.

---

## Dependency Inversion

High-level modules depend on abstractions rather than concrete implementations.

---

# Extending NotifyX

Adding a new provider requires only a few steps.

Example:

```
WhatsAppNotificationService
```

1. Implement NotificationService

2. Register the Spring Bean

3. Add the new NotificationChannel

4. Factory automatically discovers the implementation

No controller changes.

No orchestration changes.

No existing provider modifications.

---

# Architectural Goals

NotifyX was designed with the following priorities:

- Maintainability
- Extensibility
- Testability
- Loose Coupling
- High Cohesion
- Clear Separation of Concerns
- Production-inspired design

---

# Summary

NotifyX demonstrates how Spring Boot's Dependency Injection container can be used to build a clean, modular, and extensible notification platform.

Instead of tightly coupling business logic to concrete implementations, every layer communicates through abstractions, allowing new providers and features to be added with minimal changes.