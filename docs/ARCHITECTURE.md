# MidMoney — Architecture

MidMoney is a small, modular Android app that tracks stock & crypto prices (data from
[Finnhub](https://finnhub.io)). It is intentionally over-modularized for its size: the goal is to
demonstrate a **scalable, production-shaped architecture**, not to ship the smallest possible app.

## Tech stack

| Concern | Choice |
|---|---|
| Language | Kotlin 2.4.10 (built-in Kotlin support via AGP 9) |
| Build | Gradle 9.5.0, AGP 9.2.1, `build-logic` convention plugins |
| UI | Jetpack Compose, Material 3 |
| Navigation | Navigation 3 (`NavDisplay`) |
| DI | Hilt |
| Networking | Retrofit + OkHttp (+ WebSocket for live prices) |
| Persistence | DataStore (session token, preferences) |
| JVM target | 17, `minSdk` 26, `compileSdk` 37 |

## Module topology

Dependency direction is strictly one-way: **`feature → plugin → library`**. No feature depends on
another feature; no library depends on a plugin.

```
:app                     Application, MainActivity, Nav3 host, DI wiring

build-logic:convention   Gradle convention plugins (shared build config)

:library:common          Dispatchers, null-safety extensions
:library:logger          Debug-gated logging facade (MidMoneyLogger)
:library:network         Retrofit + OkHttp + Finnhub client infrastructure
:library:datastore       DataStore (session token, preferences)
:library:designsystem    Theme, typography, shared Composables
:library:navigation      Nav3 keys + Navigator abstraction
:library:mvi             MVI contract (StateFlow state, actions, effects) + delegate
:library:websocket       Generic OkHttp WebSocket transport (provider-agnostic)

:plugin:user             Auth/session via Firebase (login/logout, isLoggedIn) — cross-cutting
:plugin:market           Market + watchlist domain & data

:feature:login           UI → :plugin:user
:feature:market          UI → :plugin:market (+ :plugin:user)
:feature:detail          UI → :plugin:market
:feature:watchlist       UI → :plugin:market (+ :plugin:user)
```

### Why three module *types*?

- **`library:*`** — pure infrastructure, reusable and domain-agnostic (network, storage, design system).
- **`plugin:*`** — a self-contained business domain (`domain` + `data` layers). Reusable across features.
- **`feature:*`** — a user-facing screen slice (`presentation` layer only). Consumes a plugin's client
  facade (e.g. `MarketClient`), which wraps the repository and keeps it internal to the plugin.

This mirrors the layered structure of a real production app while keeping each module small enough to
reason about.

## Key decisions (and the reasoning)

### 1. Screens live in separate `:feature:*` modules
Compiler-enforced boundaries: a feature cannot reach into another feature's internals, only into the
shared `:library` / `:plugin` layers. For an app this small it is more than strictly necessary — the
point is to show how the codebase would scale (independent development, parallel builds, clear ownership).

### 2. `:plugin:user` instead of `:plugin:auth`
Login is a single entry flow, so it stays a `:feature:login`. But user **session and profile** data is
consumed by many features (greeting on Market, personalized Watchlist, auth token on Detail). That
cross-cutting concern is what earns a shared plugin. Login's business logic (fetch token, persist
session) lives in `:plugin:user`; the feature is only UI.

### 3. `build-logic` convention plugins
Per-module Gradle boilerplate is centralized into reusable plugins
(`midmoney.android.application/library/compose/hilt`). Adding a new module is 2–3 plugin lines instead of
40 lines of duplicated config, and SDK/Java/Kotlin settings stay consistent everywhere.

### 4. AGP 9 built-in Kotlin
AGP 9 ships Kotlin support built in, so the convention plugins deliberately do **not** apply
`org.jetbrains.kotlin.android` — applying the Android plugin is enough. Kotlin compiler options
(JVM 17) are configured via the `KotlinJvmCompile` tasks.

### 5. Navigation 3
Nav3's `NavDisplay` + typed `@Serializable` keys replace the older `NavHost`/string-route model, giving
a type-safe back stack. Two multi-module choices:

- **Shared keys** — all `Destination` keys live in `:library:navigation`, so any feature can target any
  destination without depending on another feature's implementation (avoids feature→feature coupling).
- **`Navigator` abstraction** — ViewModels inject `Navigator` and emit commands; the app owns the back
  stack and applies them. No `NavController`/Compose types leak into ViewModels.
- **Entry assembly via Hilt** — each feature contributes an `EntryProviderInstaller` into a `@IntoSet`
  multibinding; the app installs the whole set into one `entryProvider`. Adding a feature to navigation
  is just providing its installer — the app never references features directly.

### 6. MVI via delegation
Each screen has a `UiState` (StateFlow), `UiAction`s, and one-off `UiEffect`s. ViewModels implement the
`MVI` contract by delegating to a reusable `MVIDelegate` (`by mvi(initialState)`) and only override
`onAction`. Screens read state + dispatch actions through `unpackMVI()`. Navigation is driven from the
ViewModel via the injected `Navigator`; effects are reserved for transient UI (messages, dialogs).

## Convention plugins

| Plugin id | Applies to | Responsibility |
|---|---|---|
| `midmoney.android.application` | `:app` | Android application, SDK/Java/Kotlin config |
| `midmoney.android.library` | every library/plugin/feature | Android library, SDK/Java/Kotlin config |
| `midmoney.android.compose` | UI modules | Compose compiler plugin + `buildFeatures.compose` |
| `midmoney.android.hilt` | DI modules | Hilt + KSP + Hilt dependencies |
| `midmoney.android.feature` | `:feature:*` | library + compose + hilt + shared feature deps (mvi, navigation, designsystem) |

## Build

```bash
./gradlew :app:assembleDebug     # build the app
./gradlew test                   # unit tests (added in a later phase)
```
