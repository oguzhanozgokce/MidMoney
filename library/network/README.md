# :library:network

HTTP infrastructure for the app: a single configured `Retrofit` targeting the Finnhub API, plus the
shared `OkHttpClient` and `Json`. Feature/plugin modules define their own Retrofit service interfaces
and create them from the provided `Retrofit`.

## Contents

| Type | Purpose |
|---|---|
| `NetworkModule` | Hilt module providing `Json`, `OkHttpClient` and `Retrofit` as singletons. |
| `ApiKeyInterceptor` | Appends the Finnhub `token` query parameter to every request. |

## API key

The key is read at build time from `local.properties` (`finnhub.apiKey=...`) or the `FINNHUB_API_KEY`
environment variable (CI), and exposed via `BuildConfig.FINNHUB_API_KEY`. It is never committed — see
[docs/SETUP.md](../../docs/SETUP.md).

## Notes
- JSON via kotlinx-serialization (Retrofit official converter).
- HTTP body logging is enabled only in debug builds and routed through `:library:logger`
  (`AppLogger`, "Network" tag) so all requests/responses show in Logcat.
- The app targets a single provider, so the Finnhub base URL lives here. If multiple APIs were added,
  the base URL / converter setup would be generalized and per-API config moved to the consumer.
