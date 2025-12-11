# Этап сборки
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :pdf-converter:bootJar --no-daemon

# Этап запуска
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/pdf-converter/build/libs/pdf-converter-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]