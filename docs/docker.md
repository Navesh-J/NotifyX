### 📚 Documentation

- [README](../README.md)
- [Architecture](architecture.md)
- [Design Patterns](design-patterns.md)
- [API Guide](api-guide.md)
- **Docker Guide**
- [Testing](testing.md)
- [Project Structure](project-structure.md)

# 🐳 Docker Guide

## Overview

NotifyX is fully containerized using Docker.

The project includes a production-inspired Docker setup that allows the application to run consistently across different environments while keeping development simple and reproducible.

Docker eliminates the classic problem of:

> "It works on my machine."

Instead, every developer runs the same application inside the same environment.

---

# Why Docker?

Without Docker

```
Developer A

Java 21
Windows
Maven 3.9

↓

Works
```

```
Developer B

Java 17
Linux
Older Maven

↓

Fails
```

Different machines often produce different results.

---

With Docker

```
Docker Image

↓

Developer A

↓

Developer B

↓

CI/CD

↓

Production
```

Everyone runs the exact same application.

---

# Docker Components Used

NotifyX currently uses:

| Component | Purpose |
|-----------|---------|
| Dockerfile | Builds the application image |
| Docker Compose | Runs multiple services together |
| MailHog | Captures emails locally |
| Docker Network | Allows containers to communicate |
| Environment Variables | Configure runtime behaviour |

---

# Project Structure

```
NotifyX

├── Dockerfile

├── compose.yaml

├── .dockerignore

└── src/
```

---

# Dockerfile

The Dockerfile defines how the application image is built.

The project uses a **multi-stage build**.

```
Source Code

↓

Maven Build

↓

JAR

↓

Runtime Image
```

This keeps the final image significantly smaller than building everything inside a single image.

---

## Why Multi-Stage Builds?

Without multi-stage builds:

```
JDK

Maven

Source Code

Build Cache

↓

Final Image
```

Large image.

Slower deployments.

More security surface.

---

With multi-stage builds:

```
Builder Image

↓

Compile

↓

JAR

↓

Runtime Image
```

Only the application and the Java runtime are included.

Benefits:

- Smaller image
- Faster downloads
- Lower attack surface
- Faster deployment

---

# Docker Image

Build the image

```bash
docker build -t notifyx:1.0 .
```

Verify images

```bash
docker images
```

Run manually

```bash
docker run --name notifyx-app -p 8080:8080 notifyx:1.0
```

---

# Docker Compose

While Docker can run individual containers,

NotifyX uses Docker Compose to manage multiple services together.

Compose starts

```
NotifyX

↓

MailHog
```

using one command.

---

Start services

```bash
docker compose up
```

Rebuild

```bash
docker compose up --build
```

Run in background

```bash
docker compose up -d
```

Stop services

```bash
docker compose down
```

---

# Docker Compose Architecture

```
                 Docker Compose

                        │

────────────────────────────────────

NotifyX

MailHog

────────────────────────────────────

          Docker Network
```

Docker automatically creates a private network for the project.

Every service can communicate using its service name.

Example:

```
SPRING_MAIL_HOST=mailhog
```

instead of

```
localhost
```

---

# Why Not localhost?

One of the most common Docker mistakes is assuming

```
localhost
```

inside a container refers to the host machine.

It does not.

Inside Docker:

```
NotifyX Container

localhost

↓

NotifyX Container
```

To communicate with another container,

Docker DNS should be used.

```
NotifyX

↓

mailhog

↓

MailHog Container
```

The service name becomes the hostname.

---

# MailHog

NotifyX uses MailHog for local email testing.

Instead of sending real emails,

MailHog captures every message.

```
NotifyX

↓

SMTP

↓

MailHog

↓

Web Dashboard
```

MailHog UI

```
http://localhost:8025
```

SMTP

```
mailhog:1025
```

Benefits:

- No real emails sent
- Safe development
- Easy debugging
- View email content instantly

---

# Docker Networking

Compose automatically creates a network.

```
notifyx

↓

notifyx_default

↓

mailhog
```

Every service joins this network.

No manual configuration is required.

---

# Environment Variables

Runtime configuration is supplied using environment variables.

Example

```
SPRING_MAIL_HOST=mailhog
```

This overrides

```
application.yml
```

without modifying source code.

Advantages

- Environment-specific configuration
- Better portability
- Cleaner source code
- Production-ready configuration

---

# Docker Volumes

NotifyX currently does not require persistent application storage.

Future PostgreSQL integration will introduce a named Docker volume.

Example:

```
postgres-data

↓

PostgreSQL
```

Named volumes ensure database data survives container recreation.

---

# Development Workflow

A typical development session looks like this.

Build everything

```bash
docker compose up --build
```

Check running services

```bash
docker compose ps
```

View logs

```bash
docker compose logs
```

Follow logs

```bash
docker compose logs -f notifyx
```

Open a shell

```bash
docker compose exec notifyx sh
```

Stop everything

```bash
docker compose down
```

---

# Common Docker Commands

| Command | Description |
|----------|-------------|
| docker build | Build image |
| docker images | List images |
| docker ps | List running containers |
| docker compose up | Start services |
| docker compose up --build | Rebuild and start |
| docker compose up -d | Detached mode |
| docker compose logs | View logs |
| docker compose exec | Open shell inside container |
| docker compose stop | Stop containers |
| docker compose down | Remove containers and network |

---

# Troubleshooting

## Application cannot connect to MailHog

Do **not** use

```
localhost
```

Instead use

```
mailhog
```

inside Docker Compose.

---

## Port Already in Use

Check running containers

```bash
docker ps
```

or stop conflicting containers.

---

## Image Not Updating

Rebuild

```bash
docker compose up --build
```

---

## Container Keeps Exiting

Inspect logs

```bash
docker compose logs notifyx
```

---

# Production Considerations

Current Docker setup is intended for development.

Future production improvements include:

- PostgreSQL container
- Health checks
- Multi-stage production optimization
- Non-root container user
- Image scanning
- Resource limits
- Secrets management
- Docker Hub publishing
- CI/CD automation

---

# Learning Outcomes

This project demonstrates practical Docker concepts including:

- Image creation
- Multi-stage builds
- Container lifecycle
- Docker Compose
- Service orchestration
- Container networking
- Docker DNS
- Environment variables
- Logging
- Debugging
- Development workflow

---

# Summary

NotifyX uses Docker to provide a consistent, reproducible development environment.

By combining Docker, Docker Compose, and MailHog, the project demonstrates how modern Spring Boot applications are containerized for local development while remaining easily extensible for production deployment.

The Docker configuration intentionally emphasizes simplicity, clarity, and best practices, making it suitable both as a learning resource and as a foundation for future production-ready enhancements.