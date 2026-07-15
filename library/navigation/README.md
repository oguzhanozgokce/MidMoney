# :library:navigation

Navigation contract shared by all features, built on **Navigation 3**.

## Contents

| Type | Purpose |
|---|---|
| `Destination` | Sealed `NavKey` hierarchy of all destinations (`@Serializable` for back-stack persistence). |
| `Navigator` | Abstraction ViewModels depend on; emits `NavigationCommand`s as a `Flow`. |
| `DefaultNavigator` | `Channel`-backed implementation (singleton). |
| `EntryProviderInstaller` | `typealias` for a feature's entry builder, contributed via Hilt `@IntoSet`. |

## How it fits together
1. A ViewModel injects `Navigator` and calls `navigate(Destination.X)` — no Compose types in the VM.
2. The app's `NavDisplay` owns the back stack, collects `Navigator.commands`, and applies them.
3. Each feature provides an `EntryProviderInstaller` into a `Set` multibinding; the app installs them
   all into one `entryProvider`. Features never depend on each other, only on this module's keys.

See [docs/ARCHITECTURE.md](../../docs/ARCHITECTURE.md).
