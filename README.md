# Motor Desk

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-3.x-087CFA?logo=ktor&logoColor=white)
![Exposed](https://img.shields.io/badge/Exposed-1.3.0-000000)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7.10-4169E1?logo=postgresql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-5.8.0-47A248?logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-Lettuce%207.0.0-DC382D?logo=redis&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?logo=jsonwebtokens&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.x-6BA539?logo=openapiinitiative&logoColor=white)
![DDD](https://img.shields.io/badge/Architecture-DDD-blueviolet) ![Clean
Architecture](https://img.shields.io/badge/Clean-Architecture-success)

Backend API for automotive repair shop management built with **Kotlin**
and **Ktor 3**. Motor Desk manages Service Orders, customers, vehicles,
tasks, inventory items, budget approvals and asynchronous email
notifications.

## Technologies

- Kotlin
- Ktor 3
- PostgreSQL + Exposed
- MongoDB (Service Order history)
- Redis Streams (asynchronous email queue)
- JWT Authentication
- Docker Compose
- Sonar
- OpenAPI + Swagger UI

## Ubiquitous Language

- [Ubiquitous Language](docs/Ubiquitous%20Language.md)

## Architecture Decision Records

- [ADR-001 - Redis Streams](docs/ADR/ADR-001-Redis-Streams.md)
- [ADR-002 - MongoDB
  History](docs/ADR/ADR-002-MongoDB-Service-Order-History.md)

## Project Structure

``` text
src/main/kotlin
├── domain
├── application
└── infrastructure

docs
├── ADR
├── Ubiquitous Language.md
├── storytelling
└── index.html

postman
```

## Architecture

### Clean Architecture

``` mermaid
flowchart TB
Infrastructure --> Application
Application --> Domain
```

### Runtime Infrastructure

``` mermaid
flowchart LR
Client --> API[Ktor API]
API --> PostgreSQL
API --> MongoDB
API --> Redis
API --> Swagger["Swagger UI"]
```

### Service Order Flow

![img_1.png](docs/service-order-flow.png)

More about flows can be found on `docs/storytelling/`**

## Business Flows

Business diagrams are available under `docs/storytelling/`:

- [Login / Registration](docs/storytelling/Login.drawio)
- [Service Order](docs/storytelling/Service%20Order.drawio)
- [Vehicle Registration](docs/storytelling/Vehicle%20registration.drawio)
- [Forgot Password (planned)](docs/storytelling/Forgot%20Password.drawio)

## API Documentation

- Interactive Swagger UI: `http://127.0.0.1:8080/swaggerUI`
- Static OpenAPI: `docs/index.html`

### Swagger UI Preview

![Swagger UI](docs/swaggerUI.png)

### Sonar UI Preview

![Sonar UI](docs/sonar-preview.png)

Cove coverage is only on top of domain and application modules** 

## Running the Project

### Prerequisites

- JDK 21
- Docker
- Docker Compose

### Run on your machine

``` bash
docker compose -f docker-compose-dev.yml up -d
```

``` bash
./gradlew runDev
```

#### Code analyses (only works with docker-compose-dev.yml)

``` bash
./gradlew sonar
```

### Run a product-like build using docker

``` bash
docker compose -f docker-compose.yml up -d
```

### Postman Collection

Postman collection can be loaded up from this repo

### Seed users

Role Login Password
  --------------- -------------------------------- -----------
Customer

- christian.alexsander@email.com
- test@123!

Administrator

- christian.alex@email.com
- test@123!

## Useful Gradle Tasks

Command Description
  ----------------------- ----------------------
`./gradlew runDev`        Run application
`./gradlew test`          Execute tests
`./gradlew sonar`         Execute tests, coverage and upload to local sonar
`./gradlew build`         Full build
`./gradlew buildFatJar`   Build executable JAR

### Other Resources

- Ubiquitous Language: `docs/Ubiquitous Language.md`
- Storytelling diagrams: `docs/storytelling/`
- Static OpenAPI: `docs/index.html`
- Swagger UI screenshot: `docs/swaggerUI.png`
- Postman collection: `postman/`
