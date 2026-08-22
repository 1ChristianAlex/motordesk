---
applyTo: "**/domain/**/*.kt"
---

# Motor Desk - Domain Review

Review domain changes for architectural purity and business correctness.

## Dependency Rules

Domain code must not depend on Ktor, Ktor Resources, Exposed, JDBC/R2DBC, MongoDB drivers, Redis/Lettuce, Azure SDKs,
HTTP clients, or adapter configuration.

Use abstractions when an external capability is required.

## Domain Language

Use `docs/Ubiquitous Language.md` as the source of domain terminology.

## Business Rules

Business invariants should be represented in domain behavior when they are independent of transport and adapter.

Do not move business rules into Ktor routes, repositories, Redis consumers, or Azure adapters.

## Serialization

Be cautious about adding serialization/framework annotations to domain models. Verify that such dependencies are
architecturally justified. Consult relevant ADRs for documented technical debt or exceptions.

## Models

Keep domain models independent from HTTP DTOs, database entities, persistence tables, and external provider request
objects. Use explicit mapping at boundaries.

## Review Focus

Prioritize:

1. Broken domain invariants.
2. Wrong domain terminology.
3. Infrastructure leakage.
4. Unnecessary framework coupling.
5. Incorrect aggregate behavior.
6. Missing tests for business rules.
