# Этап сборки
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :eureka-server:bootJar --no-daemon

# Этап запуска
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/eureka-server/build/libs/eureka-server-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]