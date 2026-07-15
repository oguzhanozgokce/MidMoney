# Setup

## Finnhub API key

MidMoney uses the free [Finnhub](https://finnhub.io) API for market data.

1. Create a free account at [finnhub.io/register](https://finnhub.io/register).
2. Copy your API key from the dashboard.
3. Add it to `local.properties` in the project root (this file is git-ignored):

   ```properties
   finnhub.apiKey=YOUR_API_KEY_HERE
   ```

4. Sync/build. The key is read at build time and exposed via `BuildConfig.FINNHUB_API_KEY` in
   `:library:network`; it is never committed to git.

For CI, provide the key through the `FINNHUB_API_KEY` environment variable instead of `local.properties`.

## Build

```bash
./gradlew :app:assembleDebug
```

## Code quality

Formatting is enforced with **ktlint** and static analysis with **detekt** (config in
`config/detekt/detekt.yml`), applied to every module.

```bash
./gradlew ktlintFormat     # auto-fix formatting
./gradlew ktlintCheck       # verify formatting
./gradlew detekt            # static analysis
```

### Pre-commit hook

A pre-commit hook runs `ktlintCheck` + `detekt` before each commit. Enable it once per clone:

```bash
./gradlew installGitHooks   # or: git config core.hooksPath .githooks
```

### CI

GitHub Actions (`.github/workflows/ci.yml`) runs ktlint, detekt, unit tests and a debug build on every
push and pull request to `main`. Building does not require the Finnhub key; add a `FINNHUB_API_KEY`
secret only if a workflow needs live data.

## Requirements

- JDK 17
- Android SDK: `compileSdk` 37, `minSdk` 26
- Gradle 9.5 (via the wrapper), AGP 9.2.1
