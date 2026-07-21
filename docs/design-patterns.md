### 📚 Documentation

- [README](../README.md)
- [Architecture](architecture.md)
- **Design Patterns**
- [API Guide](api-guide.md)
- [Docker Guide](docker.md)
- [Testing](testing.md)
- [Project Structure](project-structure.md)

# 🎯 Design Patterns Used in NotifyX

## Introduction

NotifyX was intentionally designed as more than a notification application.

Its primary objective is to demonstrate how multiple software design patterns work together in a real Spring Boot application.

Instead of implementing patterns in isolation, NotifyX combines them to solve a practical business problem:

> Deliver notifications through multiple channels while keeping the system modular, extensible, and easy to maintain.

The project demonstrates how Spring's Dependency Injection container naturally complements classic object-oriented design patterns.

---

# Pattern Overview

| Pattern | Purpose |
|----------|---------|
| Strategy | Multiple notification providers implementing a common interface |
| Factory | Selects the correct provider at runtime |
| Composite | Sends notifications through multiple providers simultaneously |
| Dependency Injection | Decouples implementations from consumers |
| Repository | Abstracts persistence operations |
| Gateway | Encapsulates external communication |
| AOP | Separates logging from business logic |
| Retry | Handles transient failures gracefully |

---

# 1. Strategy Pattern

## Problem

An application needs to support multiple notification channels.

For example:

- Email
- SMS
- Push Notification

A poor implementation would use:

```java
if(channel == EMAIL){

}
else if(channel == SMS){

}
else{

}
```

Every new provider would require modifying existing business logic.

This violates the **Open/Closed Principle**.

---

## Solution

NotifyX defines a common contract:

```
NotificationService
```

Every provider implements this interface.

```
NotificationService

      ▲

────────────────────────────

EmailNotificationService

SmsNotificationService

PushNotificationService
```

Each implementation knows only how to deliver its own notification type.

The rest of the application depends only on the interface.

---

## Benefits

- Easy to extend
- Loosely coupled
- Easy to test
- Runtime provider selection
- Cleaner code

---

# 2. Factory Pattern

## Problem

Even with the Strategy Pattern, the application still needs to choose the correct strategy.

Without a factory:

```
Controller

↓

switch(channel)

↓

Email

SMS

Push
```

Every controller or service would contain provider-selection logic.

---

## Solution

NotifyX introduces

```
NotificationServiceFactory
```

```
NotificationApplicationService

            │

            ▼

NotificationServiceFactory

            │

────────────┼────────────

            ▼

NotificationService
```

The factory is responsible for returning the appropriate implementation.

Business logic never knows which concrete class is returned.

---

## Benefits

- Centralized provider resolution
- Easy maintenance
- Cleaner services
- Better scalability

---

# 3. Composite Pattern

## Problem

Sometimes the application must send one notification through every available provider.

Without a composite implementation:

```
Email.send()

Sms.send()

Push.send()
```

would appear throughout the application.

---

## Solution

NotifyX introduces

```
CompositeNotificationService
```

```
CompositeNotificationService

        │

──────────────────────────

Email

SMS

Push

──────────────────────────
```

Spring injects every available

```
NotificationService
```

implementation into the composite.

The composite iterates through them and aggregates the results.

---

## Benefits

- Broadcast capability
- No duplicated orchestration
- Automatic provider discovery
- Easy extensibility

---

# 4. Dependency Injection

Dependency Injection is the foundation of NotifyX.

Spring creates every object and wires dependencies automatically.

```
Spring Container

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

      ▼

EmailNotificationService
```

The application never manually creates services using

```java
new
```

Instead, Spring manages the object lifecycle.

---

## Constructor Injection

Used for mandatory dependencies.

Advantages:

- Immutable objects
- Easier testing
- Better readability
- Recommended Spring practice

---

## Collection Injection

Spring automatically injects every implementation of

```
NotificationService
```

into

```
CompositeNotificationService
```

This makes broadcast delivery possible without hardcoding providers.

---

## Configuration Injection

Configuration properties are injected through Spring rather than hardcoded.

This allows different runtime configurations without changing the source code.

---

# 5. Repository Pattern

Persistence responsibilities belong exclusively to the repository layer.

```
AuditService

↓

AuditRepository

↓

Database
```

Business services never execute SQL directly.

Advantages:

- Cleaner architecture
- Easier testing
- Swappable persistence layer
- Better separation of concerns

---

# 6. Gateway Pattern

NotifyX communicates with external providers through gateway interfaces.

```
Email Service

↓

JavaMailSender
```

```
Sms Service

↓

SmsGateway
```

```
Push Service

↓

PushGateway
```

Instead of coupling business logic to external vendors, the application depends on abstractions.

This makes provider replacement straightforward.

---

# 7. Aspect-Oriented Programming (AOP)

Logging is a cross-cutting concern.

Without AOP:

```
log.info()

Business Logic

log.info()
```

would appear inside nearly every method.

Instead, NotifyX centralizes logging inside

```
NotificationLoggingAspect
```

```
Method Call

↓

Aspect

↓

Business Logic

↓

Aspect
```

The business layer remains focused solely on business rules.

---

## Benefits

- Cleaner services
- Consistent logging
- Better maintainability
- Separation of concerns

---

# 8. Retry Pattern

External systems occasionally fail.

Rather than immediately reporting failure, NotifyX retries notification delivery.

```
Attempt

↓

Failure

↓

Retry

↓

Success
```

Retry logic is isolated from delivery logic, making providers simpler and easier to maintain.

---

# SOLID Principles in NotifyX

## Single Responsibility Principle

Every class has one clearly defined responsibility.

Examples:

- Controller handles HTTP
- Factory selects providers
- Provider delivers notifications
- Repository persists audit logs

---

## Open/Closed Principle

Adding a new provider requires creating a new implementation rather than modifying existing ones.

Example:

```
WhatsAppNotificationService
```

No controller changes.

No service changes.

---

## Liskov Substitution Principle

Every provider can replace another because they implement the same contract.

---

## Interface Segregation Principle

Clients depend only on operations they require.

Notification providers implement only the notification contract.

---

## Dependency Inversion Principle

High-level modules depend on

```
NotificationService
```

rather than

```
EmailNotificationService
```

This greatly reduces coupling.

---

# How the Patterns Work Together

One notification request flows through several patterns.

```
Client

↓

Controller

↓

Application Service

↓

Factory

↓

Strategy

↓

Gateway

↓

Audit

↓

Repository
```

For broadcast delivery:

```
Client

↓

Composite

↓

Email

SMS

Push

↓

Aggregate Response
```

Instead of competing with each other, the patterns complement one another.

---

# Why These Patterns?

NotifyX intentionally demonstrates patterns commonly encountered in enterprise Spring Boot applications.

The project emphasizes:

- Extensibility
- Testability
- Maintainability
- Loose coupling
- High cohesion
- Clean architecture
- Real-world Spring practices

The goal is not simply to "use design patterns," but to show how they solve practical engineering problems in a modular backend system.

---

# Summary

NotifyX combines Spring Boot's Dependency Injection container with several classic design patterns to build a notification platform that is flexible, extensible, and production-inspired.

Each pattern has a clearly defined responsibility, and together they create an architecture that is significantly easier to understand, maintain, and extend than a tightly coupled implementation.