# syntax=docker/dockerfile:1

###############################
# Build Stage
###############################
FROM gradle:9.6.0-jdk21 AS builder

WORKDIR /home/gradle/project

COPY ../../gradle gradle
COPY ../../gradlew .
COPY ../../settings.gradle.kts .
COPY ../../build.gradle.kts .
COPY ../../gradle/libs.versions.toml gradle/libs.versions.toml

RUN chmod +x gradlew

# Cache das dependências
RUN ./gradlew dependencies --no-daemon

# Código da aplicação
COPY ../.. .

# Gera a distribuição da aplicação
RUN ./gradlew installDist --no-daemon

###############################
# Runtime Stage
###############################
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder \
    /home/gradle/project/build/install/*/ ./

EXPOSE 8080

ENTRYPOINT ["bin/motordesk"]