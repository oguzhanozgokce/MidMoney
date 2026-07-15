# :library:event

Analytics/event tracking, backed by Firebase Analytics.

## Contents

| Type | Purpose |
|---|---|
| `Analytics` | Facade ViewModels depend on: `track(event)`, `setUserId(id)`. |
| `AnalyticsEvent` | Interface (name + params); each feature defines its own events by implementing it. |
| `FirebaseAnalyticsClient` | Firebase-backed implementation. |

Firebase stays internal here; features only see `Analytics` and their own event types. Requires the
Firebase setup in [docs/SETUP.md](../../docs/SETUP.md).
