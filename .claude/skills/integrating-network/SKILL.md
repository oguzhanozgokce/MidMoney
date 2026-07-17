---
name: integrating-network
description: >
  How to add a network integration in MidMoney: Retrofit API, serializable DTOs, domain
  mappers, a repository wrapped in ErrorHandler, Hilt wiring, and error mapping via
  AppError/ErrorMapper. Use when adding an endpoint or a new failure type.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Integrating Network

Network work lives in a `plugin` (business domain) that depends on `library:network`
(Retrofit/OkHttp) and `library:error` (ErrorHandler, AppError).

## Steps

1. **DTO** — `data/remote/dto/XDto.kt`, `@Serializable`, nullable where the API is not
   guaranteed. DTOs never leave the data layer.
2. **API** — `data/remote/XApi.kt`, a Retrofit interface. Suspend functions returning DTOs.
3. **Mapper** — `toDomain()` extension converting DTO → domain model. All formatting/derivation
   happens here, not in the ViewModel or a `@Composable`.
4. **Repository** — `interface XRepository` (domain) + `XRepositoryImpl` (data). Every call is
   wrapped in `ErrorHandler.call { }` and returns `Result<T>`.
5. **Client** — a `plugin` facade (`XClient`) the feature layer calls. Never expose the repo to UI.
6. **DI** — one Hilt module: `@Provides` the API from Retrofit, `@Binds` impl → interface.

```kotlin
class XRepositoryImpl @Inject constructor(
    private val api: XApi,
    private val errorHandler: ErrorHandler,
) : XRepository {
    override suspend fun get(id: String): Result<X> =
        errorHandler.call { api.get(id).toDomain() }
}
```

## Base URL and config

- The base URL comes from `AppConfig` (injected), which the `app` provides from flavor
  `BuildConfig`. Do not read `BuildConfig` for the URL inside `library:network` or a plugin.
- Retrofit is provided once in `library:network`'s `NetworkModule`, using `appConfig.baseUrl`.
- The API key is injected via `ApiKeyInterceptor`. Never hardcode secrets; read from local.properties.

## Error handling

- Do not `try`/`catch` or `runCatching` in the repository. `ErrorHandler` catches, applies the
  dispatcher, and converts failures to `AppException(AppError)`.
- Transport failures are classified by `NetworkErrorMapper` (`library:network`): timeout →
  `Timeout`, no connectivity → `Network`, 401 → `Unauthorized`, other HTTP → `Server(code)`.
- To recognize a new failure source, add an `ErrorMapper` with `@Binds @IntoSet` in that
  source's module. Do not add cases to `NetworkErrorMapper` for non-network errors.
- Add a new `AppError` case only when the UI must react to it differently; give it a message
  in `library:error` (`messageRes`) with EN + TR strings.

## Presentation

- The ViewModel consumes `Result<T>` and, on failure, sets
  `errorMessage = UiText.Resource(throwable.errorMessageRes())`.
- The ViewModel never inspects `AppError` types with a `when` — the mapping lives in `library:error`.

## Logging

- Requests are logged by `NetworkLoggingInterceptor` in debug builds only. Do not add ad-hoc logging.

## Red flags

- `runCatching` / `try`/`catch` in a repository.
- A DTO or `HttpException` leaking into domain or presentation.
- A hardcoded base URL or API key.
- A `when (appError)` block inside a ViewModel.
