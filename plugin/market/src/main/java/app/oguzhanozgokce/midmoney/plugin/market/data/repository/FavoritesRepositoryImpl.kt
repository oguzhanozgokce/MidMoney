package app.oguzhanozgokce.midmoney.plugin.market.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : FavoritesRepository {

    override val favoriteSymbols: Flow<Set<String>> =
        dataStore.data.map { prefs -> prefs[KEY].orEmpty() }

    override suspend fun toggle(symbol: String) {
        dataStore.edit { prefs ->
            val current = prefs[KEY].orEmpty()
            prefs[KEY] = if (symbol in current) current - symbol else current + symbol
        }
    }

    private companion object {
        val KEY = stringSetPreferencesKey("watchlist_symbols")
    }
}
