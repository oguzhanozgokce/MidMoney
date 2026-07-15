# :library:logger

A tiny logging facade over `android.util.Log`.

## Contents

| Type | Purpose |
|---|---|
| `AppLogger` | `d`/`i`/`w`/`e` helpers, gated on `BuildConfig.DEBUG` so logs appear only in debug builds. |

- Default tag: `MidMoney`. Network traffic is logged with the `Network` tag (see `:library:network`).
- Kept as a simple object for zero-ceremony use; can be swapped for Timber or wrapped behind a DI
  interface later without touching call sites much.
