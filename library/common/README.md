# :library:common

Domain-agnostic building blocks shared across every layer. No Android UI, no business logic.

## Contents

| Type | Purpose |
|---|---|
| `DispatcherProvider` | Abstraction over coroutine dispatchers (`io`/`default`/`main`) so use cases and repositories are unit-testable with a test dispatcher. `DefaultDispatcherProvider` is the production implementation. |
| `PrimitiveExtensions` | Null-safety helpers (`Int?.orZero()`, `Boolean?.orFalse()`, …) for concise mappers. |

For success/error results we use the standard library `kotlin.Result<T>` (an allocation-free inline
value class) rather than a custom wrapper — no bespoke type until a specific need (typed domain errors,
or an exhaustive UI state) actually appears.

## Depends on
- `kotlinx-coroutines-core`

Consumed by `:plugin:*` and `:feature:*` modules.
