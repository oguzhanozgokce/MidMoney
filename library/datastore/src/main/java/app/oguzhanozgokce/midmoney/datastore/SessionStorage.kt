package app.oguzhanozgokce.midmoney.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Persists the user session token. Exposed as a [Flow] so consumers can react to
 * login/logout, and backed by DataStore for async, transactional storage.
 */
interface SessionStorage {
    val token: Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clear()
}

class DataStoreSessionStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SessionStorage {

    override val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences -> preferences[TOKEN_KEY] = token }
    }

    override suspend fun clear() {
        dataStore.edit { preferences -> preferences.remove(TOKEN_KEY) }
    }

    private companion object {
        val TOKEN_KEY = stringPreferencesKey("session_token")
    }
}
