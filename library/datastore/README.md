# :library:datastore

Local key-value persistence built on Jetpack DataStore (Preferences).

## Contents

| Type | Purpose |
|---|---|
| `SessionStorage` | Stores the user session token; exposes it as a `Flow<String?>` so features react to login/logout. |
| `DataStoreModule` | Provides the singleton `DataStore<Preferences>`. |
| `StorageModule` | Binds `SessionStorage` to its DataStore-backed implementation. |

Consumed by `:plugin:user` for session management.
