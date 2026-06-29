# ADR-003 - Technical Debt: `@Serializable` Annotation in the Domain Layer

## Status

Opened (Technical Debt)

---

## Context

The project follows **Domain-Driven Design (DDD)** and a layered architecture, where the **Domain** layer should remain independent of infrastructure and framework-specific concerns.

During the implementation of the Service Order history, domain models needed to be serialized for persistence in MongoDB and for message exchange through Redis Streams.

To simplify the implementation within the project's time constraints, the Kotlin Serialization annotation (`@Serializable`) was added directly to several domain classes.

Although this approach reduced development effort, it introduced an unwanted dependency between the Domain layer and the `kotlinx.serialization` framework.

---

## Problem

The Domain layer is now aware of a serialization technology.

This creates a violation of the Dependency Rule, since business entities should not depend on infrastructure concerns.

Consequences include:

* coupling between the domain model and Kotlin Serialization;
* reduced portability of the domain model;
* more difficult replacement of the serialization framework;
* reduced separation of concerns.

---

## Decision

The project will temporarily keep the `@Serializable` annotations in the Domain layer.

This decision was made to meet the academic project's deadline while avoiding unnecessary complexity during the initial implementation.

The issue is recognized as **technical debt** and should be addressed in a future refactoring.

---

## Proposed Solution

Serialization concerns should be moved to the Infrastructure layer.

Instead of serializing domain entities directly, dedicated persistence models (Documents) and message models (Events/DTOs) should be introduced.

Mapping between Domain objects and persistence/message models should be performed through dedicated mapper classes.

---

## Benefits

After the refactoring:

* the Domain layer becomes framework-independent;
* serialization libraries can be replaced without affecting business logic;
* better compliance with DDD and Clean Architecture principles;
* improved separation of concerns;
* easier unit testing of the Domain layer.

---

## Drawbacks

The proposed solution introduces:

* additional DTO classes;
* mapper implementations;
* increased amount of boilerplate code.

However, these drawbacks are considered acceptable in exchange for a cleaner architecture.

---

## Migration Plan

1. Remove `@Serializable` from all Domain classes.
2. Implement mappers between Domain and Infrastructure models.
3. Update repositories and publishers to use the new models.
4. Remove the dependency on Kotlin Serialization from the Domain module.

---

## Priority

**Medium**

The current implementation is stable and does not compromise business functionality. The refactoring should be scheduled when architectural improvements become a priority over feature development.

---

## Consequences

### Current

* Faster development.
* Simpler serialization.
* Additional framework dependency in the Domain layer.

### Future

* Cleaner architecture.
* Better maintainability.
* Lower coupling between business rules and infrastructure.
