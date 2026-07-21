### 📚 Documentation

- [README](../README.md)
- [Architecture](architecture.md)
- [Design Patterns](design-patterns.md)
- **API Guide**
- [Docker Guide](docker.md)
- [Testing](testing.md)
- [Project Structure](project-structure.md)

# 🌐 API Guide

## Overview

NotifyX exposes a RESTful API for sending notifications through multiple delivery channels.

The API is intentionally simple while demonstrating clean request validation, layered architecture, consistent response models, and proper HTTP status codes.

All endpoints return JSON.

---

# Base URL

Local Development

```text
http://localhost:8080
```

Docker

```text
http://localhost:8080
```

---

# API Flow

Every request follows the same lifecycle.

```
Client

↓

Controller

↓

Validation

↓

Application Service

↓

Factory

↓

Notification Service

↓

Gateway

↓

Audit Logging

↓

JSON Response
```

---

# Authentication

Currently, the API does **not** require authentication.

Future versions may include:

- JWT Authentication
- OAuth2
- API Keys
- Role-Based Access Control

---

# Content Type

Every request should include

```http
Content-Type: application/json
```

---

# Notification Channels

Supported channels

| Channel | Description |
|----------|-------------|
| EMAIL | Sends an email notification |
| SMS | Sends an SMS notification |
| PUSH | Sends a push notification |

---

# Send Notification

## Endpoint

```http
POST /api/notifications/send
```

---

## Description

Sends a notification using a single delivery channel.

The notification provider is selected dynamically using the NotificationServiceFactory.

---

## Request Body

```json
{
  "channel": "EMAIL",
  "recipient": "john@example.com",
  "subject": "Welcome",
  "message": "Welcome to NotifyX!"
}
```

---

## Field Description

| Field | Required | Description |
|---------|----------|-------------|
| channel | ✅ | Notification channel |
| recipient | ✅ | Target recipient |
| subject | Optional | Email subject |
| message | ✅ | Notification content |

---

## Example Response

```json
{
  "status": "SUCCESS",
  "messageId": "d95cfe7e-7cb6-4b0d-a6ef-15d5df9d7b0d",
  "provider": "EMAIL",
  "timestamp": "2026-07-22T18:12:43Z"
}
```

---

## Success Status

```http
200 OK
```

---

## Possible Errors

```http
400 Bad Request
```

Validation failed.

---

```http
404 Not Found
```

Requested provider unavailable.

---

```http
500 Internal Server Error
```

Unexpected server failure.

---

# Broadcast Notification

## Endpoint

```http
POST /api/notifications/send-all
```

---

## Description

Broadcasts one notification through every registered provider.

Spring automatically injects every NotificationService implementation into CompositeNotificationService.

---

## Request Body

```json
{
    "recipient":"john@example.com",
    "subject":"Maintenance",
    "message":"The service will be unavailable tonight."
}
```

---

## Example Response

```json
{
  "successfulChannels": 3,
  "failedChannels": 0,
  "results": [
    {
      "channel": "EMAIL",
      "status": "SUCCESS"
    },
    {
      "channel": "SMS",
      "status": "SUCCESS"
    },
    {
      "channel": "PUSH",
      "status": "SUCCESS"
    }
  ]
}
```

---

# Validation

NotifyX uses Jakarta Bean Validation.

Typical validation includes

- Required fields
- Empty message checks
- Recipient validation
- Channel validation

Example invalid request

```json
{
    "channel":"",
    "recipient":"",
    "message":""
}
```

Example response

```json
{
    "timestamp":"2026-07-22T18:10:25",
    "status":400,
    "error":"Validation Failed",
    "message":"Recipient must not be blank"
}
```

---

# Error Handling

NotifyX centralizes exception handling through

```
GlobalExceptionHandler
```

Every error follows a consistent response format.

Example

```json
{
    "timestamp":"2026-07-22T18:12:44",
    "status":500,
    "error":"Notification Delivery Failed",
    "message":"Unable to deliver notification."
}
```

---

# HTTP Status Codes

| Status | Meaning |
|----------|---------|
| 200 | Request completed successfully |
| 400 | Validation error |
| 404 | Resource/provider not found |
| 500 | Internal server error |

---

# OpenAPI Documentation

NotifyX automatically generates interactive API documentation.

Swagger UI

```
http://localhost:8080/swagger-ui
```

OpenAPI Specification

```
http://localhost:8080/v3/api-docs
```

Swagger allows developers to

- Explore endpoints
- Execute requests
- Inspect request bodies
- Inspect response schemas
- Verify validation rules

without writing any client code.

---

# MailHog Integration

When Email notifications are sent locally,

MailHog captures the outgoing messages instead of sending real emails.

MailHog Dashboard

```
http://localhost:8025
```

This allows safe testing during development.

---

# Example Workflow

```
POST /send

↓

Controller

↓

Validation

↓

Application Service

↓

Factory

↓

Email Service

↓

JavaMailSender

↓

MailHog

↓

Audit Log

↓

Response
```

---

# API Design Principles

NotifyX follows several REST best practices.

✔ Resource-oriented endpoints

✔ JSON request/response

✔ Layered architecture

✔ Proper HTTP status codes

✔ Consistent error responses

✔ Request validation

✔ OpenAPI documentation

✔ Separation of HTTP and business logic

---

# Testing the API

The API can be tested using

- Swagger UI
- Postman
- Insomnia
- curl
- HTTP Client (IntelliJ)

Example

```bash
curl -X POST \
http://localhost:8080/api/notifications/send \
-H "Content-Type: application/json" \
-d '{
    "channel":"EMAIL",
    "recipient":"john@example.com",
    "subject":"Hello",
    "message":"Welcome to NotifyX"
}'
```

---

# Future API Enhancements

Future versions of NotifyX may include

- JWT Authentication
- Rate Limiting
- API Versioning
- Batch Notification APIs
- Notification History APIs
- Search APIs
- Pagination
- Async Notification Processing
- Webhooks
- Kafka Event Publishing

---

# Summary

NotifyX exposes a clean, RESTful API designed around simplicity, consistency, and extensibility.

The API demonstrates proper Spring Boot practices including request validation, centralized exception handling, layered architecture, OpenAPI documentation, and dependency-driven provider selection while remaining easy to consume for both developers and automated clients.