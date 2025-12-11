# Этап сборки
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :flow-manager:bootJar --no-daemon

# Этап запуска
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/flow-manager/build/libs/flow-manager-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]