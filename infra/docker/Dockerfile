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

#ENV DATABASE_URL=jdbc:postgresql://sql-database:5432/motordesk
#ENV DATABASE_USER=motordesk
#ENV DATABASE_PASSWORD=motordesk_pass
#ENV DATABASE_DRIVER=org.postgresql.Driver
#ENV MONGO_URL=mongodb://mongodb:27017
#ENV MONGO_USER=motordesk
#ENV MONGO_PASSWORD=motordesk_pass
#ENV MONGO_DATABASE=motordesk
#ENV REDIS_HOST=redis
#ENV REDIS_PORT=6379
#ENV REDIS_PASSWORD=motordesk_pass
#ENV AZURE_COMMUNICATION_ACCESS_KEY=
#ENV AZURE_COMMUNICATION_ENDPOINT=
#ENV JWT_ISSUER=
#ENV JWT_AUDIENCE=
#ENV JWT_REALM=
#ENV JWT_SECRET=
#ENV HTTP_HOST=0.0.0.0
#ENV HTTP_PORT=8080
#ENV HTTP_PROTOCOL=http

COPY --from=builder \
    /home/gradle/project/build/install/*/ ./

EXPOSE 8080

ENTRYPOINT ["bin/motordesk"]
