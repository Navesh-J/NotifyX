# ---------- Builder Stage ----------
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn ./.mvn

RUN ./mvnw clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]