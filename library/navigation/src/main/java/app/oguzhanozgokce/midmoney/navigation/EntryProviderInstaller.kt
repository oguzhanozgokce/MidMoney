package app.oguzhanozgokce.midmoney.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

/**
 * A feature contributes its Nav3 entries by providing one of these into a Hilt `@IntoSet`
 * multibinding. The app collects the whole set and installs every builder into a single
 * `entryProvider`, so features plug into navigation without the app knowing about them directly.
 */
typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit
