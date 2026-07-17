package app.oguzhanozgokce.midmoney.plugin.market.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    val favoriteSymbols: Flow<Set<String>>

    suspend fun toggle(symbol: String)
}
