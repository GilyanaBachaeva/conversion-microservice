plugins {
    java
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "spring-micro"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

tasks.bootJar {
    mainClass.set("spring_micro.eureka_server.EurekaServerApplication")
}