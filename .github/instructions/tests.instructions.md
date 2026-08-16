---
applyTo: "**/test/**/*.kt,**/tests/**/*.kt,**/*Test.kt,**/*Tests.kt"
---

# Motor Desk - Test Review

Review tests for behavioral coverage and architectural boundaries.

## Test Behavior

Prefer observable behavior over implementation details.

For new functionality consider:
- happy path;
- validation failures;
- authorization failures;
- persistence failures;
- external service failures;
- retry behavior;
- boundary conditions;
- duplicate/repeated operations.

## Domain Tests

Domain tests should not require infrastructure unless explicitly integration tests.

## Integration Tests

Make dependencies on PostgreSQL, MongoDB, Redis, Ktor HTTP, or external adapters explicit.

## Email

For email functionality test message mapping, successful delivery, provider failure, retry count, maximum retry behavior, terminal failure, worker processing, and persistence state transitions.

Do not require a real Azure Communication Services account for ordinary unit tests.

## Security

For tokenized approval flows test valid, invalid, expired, revoked, already-used, wrong-purpose tokens, and authorization/state validation.

Never put real credentials or production secrets in tests.
