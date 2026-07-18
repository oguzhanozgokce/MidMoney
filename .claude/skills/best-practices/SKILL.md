---
name: best-practices
description: >
  Foundational conventions for MidMoney: layering, MVI ViewModels, repositories with
  ErrorHandler, mappers, DI, test tags, and analytics event constants. Read this first
  for any implementation, refactor, or review task.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# MidMoney Best Practices

Read this before any code change. Other skills build on it.

## Layering

- `feature` = UI + ViewModel. `plugin` = domain + data (business). `library` = infra.
- Direction: `feature → plugin → library`. Never `feature → feature`, never `library → plugin`.
- Domain is pure Kotlin. No Android, Retrofit, Firebase, or Compose types in domain models or interfaces.
- UI calls a plugin's client/use case, never a repository directly.

## MVI

- ViewModel delegates: `MVI<UiState, UiAction, UiEffect> by mvi(UiState())`.
- Mutate with `updateUiState { copy(...) }`. Emit one-shot events with `emitUiEffect(...)`.
- Handle input in `onAction(uiAction)`. Screens call `onAction`, never touch the repository.
- ViewModels are Context-free. No `Context`, no `stringResource`, no Android resource lookup.
- User-facing text is `UiText` (`Resource(id, args)` or `Dynamic(value)`), resolved in the UI.

## Navigation

- Inject `Navigator` into the ViewModel: `navigator.navigate(Destination.X)` / `goBack()`.
- `Destination` types live in `library:navigation`. NavController stays in `app` (Navigation3 host).
- Split UI: `XRoute` collects state + effects; `XScreen` is stateless and preview-friendly.

## Repository + error handling

- Repositories return `Result<T>`. Wrap every call in `ErrorHandler.call { ... }`.
- Never write `withContext { runCatching { } }` in a repository — `ErrorHandler` owns dispatcher + catch.
- Failures are `AppException(AppError)`. Classification happens in `ErrorMapper`, not the ViewModel.
- In the ViewModel, map to a message with `throwable.errorMessageRes()` wrapped in `UiText.Resource`.
- Name: `interface XRepository` / `class XRepositoryImpl`. A plugin exposes a `XClient` facade.

```kotlin
override suspend fun getQuote(symbol: String): Result<Quote> =
    errorHandler.call { api.getQuote(symbol).toDomain(symbol) }
```

## Mappers

- Extension functions: `toDomain()` in data, `toUi()` in presentation/ui.
- Do all formatting (price, percent, dates) in the mapper — never inside a `@Composable`.
- DTOs are `@Serializable`, live in `data/remote/dto`, and never leave the data layer.

## Dependency injection (Hilt)

- One Hilt module per plugin/library. `@Binds` impl → interface; `@Provides` for framework types.
- Extend a set with `@Binds @IntoSet` (see `ErrorMapper`). Declare empty sets with `@Multibinds`.
- ViewModels use `@HiltViewModel` + constructor injection.

## Test tags (for Maestro)

- Every interactive or asserted composable gets `Modifier.testTag(...)`.
- Keys live in a per-feature object, dot-namespaced, never inline literals:

```kotlin
object LoginTestTags {
    const val EMAIL = "login.email"
    const val PASSWORD = "login.password"
    const val SUBMIT = "login.submit"
}
```

- Maestro selects with `id: login.email`. Keep tags stable; they are a test contract.

## Analytics events & magic strings

- Events implement `AnalyticsEvent` in `feature/<name>/analytics/`. Track via injected `Analytics`.
- Event names and param keys are `private const val`, not inline string literals.
- The same rule applies to any repeated magic string (categories, keys, tags).
- `Analytics.track(event, vararg suppliers)` routes per call: `track(event, EventSupplier.Firebase)`
  hits only Firebase; omitting suppliers defaults to Firebase. Each backend is an `AnalyticsTracker`
  that declares its `EventSupplier` and is registered with `@Binds @IntoSet`; `CompositeAnalytics`
  dispatches by supplier. Add Superset/Amplitude with one tracker + a new `EventSupplier` — no
  caller changes.

## Design system

- Reuse `MidMoney*` components. Do not hand-roll a Button, TextField, or empty state.
- No hardcoded colors. Use `MaterialTheme.colorScheme` or `MidMoneyTheme.extraColors`.
- No hardcoded user strings. Use `stringResource` in UI and `UiText` from ViewModels.

## Before you finish

Run: `./gradlew ktlintFormat detekt :app:assembleProdDebug test`. All must pass.

## Red flags

- `runCatching` / `withContext` inside a repository.
- `Context` or `stringResource` in a ViewModel.
- Formatting or business logic inside a `@Composable`.
- A repository called directly from a screen.
- Inline test tags or event-name literals.
