---
name: creating-a-module
description: >
  How to add a Gradle module to MidMoney (library, plugin, or feature): choosing the
  type, wiring settings/build files with convention plugins, dependency rules, DI, and
  the correct build order. Use when adding a module or editing build-logic or flavors.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Creating a Module

## 1. Choose the type

| Type | Contains | Depends on |
|---|---|---|
| `library:<name>` | Pure infra, reusable, domain-agnostic | other `library` only |
| `plugin:<name>` | A business domain: domain + data + client | `library` |
| `feature:<name>` | One screen area: UI + ViewModel | `plugin`, `library` |

Rules: `feature → plugin → library`. Never `feature → feature`, never `library → plugin`.
If it holds business rules, it is a `plugin`. If it is generic tooling, it is a `library`.

## 2. Wire Gradle

Add to `settings.gradle.kts`, keeping groups ordered:

```kotlin
include(":library:<name>")   // or :plugin:<name> / :feature:<name>
```

Create `build.gradle.kts` using the matching convention plugins:

```kotlin
plugins {
    alias(libs.plugins.midmoney.android.library)   // or .application
    alias(libs.plugins.midmoney.android.hilt)      // if it uses DI
    alias(libs.plugins.midmoney.android.compose)    // if it has Compose UI
}

android { namespace = "app.oguzhanozgokce.midmoney.<name>" }

dependencies {
    implementation(project(":library:common"))
    // add only what you use
}
```

Feature modules use `libs.plugins.midmoney.android.feature`. Never re-declare compileSdk,
minSdk, or Java version — the convention plugins own those.

## 3. Build order inside the module

1. **domain** — pure Kotlin: models, repository interface, client facade. No Android types.
2. **data** — `data/remote/dto` (`@Serializable`), mappers (`toDomain()`), `RepositoryImpl`.
3. **di** — one Hilt module: `@Binds` impl → interface, `@Provides` for framework types.
4. **presentation** (feature only) — see `composing-screens`.

Repositories return `Result<T>` via `ErrorHandler.call { }`. No `runCatching`/`withContext`.

## 4. Cross-cutting extension points

When a module introduces a new transport or failure source, contribute an `ErrorMapper`
instead of handling errors locally:

```kotlin
class XErrorMapper @Inject constructor() : ErrorMapper {
    override fun map(throwable: Throwable): AppError? = /* map or null */
}

@Module @InstallIn(SingletonComponent::class)
abstract class XErrorModule {
    @Binds @IntoSet abstract fun bind(impl: XErrorMapper): ErrorMapper
}
```

Keep the generic contract (`AppError`, `ErrorHandler`) in `library:error`. Put transport-
specific mapping in that transport's module (e.g. `NetworkErrorMapper` in `library:network`).

## 5. build-logic and flavors

- Convention plugins live in `build-logic/convention` and are registered in its `build.gradle.kts`.
- Shared build behavior (flavors, buildConfig, Compose, Hilt) belongs in a convention plugin,
  not copied into each module.
- Flavors are defined once in `Flavor.kt` (`configureFlavors`) and applied by the application
  convention plugin. Per-flavor config flows through `AppConfig` via DI, not read from BuildConfig
  in library modules.

## 6. Finish

- Add the new module to its consumers' `dependencies`.
- Run `./gradlew ktlintFormat detekt :app:assembleProdDebug test`.

## Consider a subagent

For a full module (domain → data → DI → wiring), a scoped agent can scaffold it end to end.
Give it this skill plus `best-practices`, and have it stop at a green build.

## Red flags

- A `library` depending on a `plugin`.
- Business logic placed in a `library`.
- compileSdk / minSdk / Java version duplicated in a module build file.
- A library module reading flavor `BuildConfig` directly instead of injecting `AppConfig`.
