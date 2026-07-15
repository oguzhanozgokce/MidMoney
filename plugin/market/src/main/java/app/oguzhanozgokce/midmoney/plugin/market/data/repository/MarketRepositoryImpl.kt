package app.oguzhanozgokce.midmoney.plugin.market.data.repository

import app.oguzhanozgokce.midmoney.common.coroutines.DispatcherProvider
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.FinnhubApi
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.toDomain
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val api: FinnhubApi,
    private val dispatchers: DispatcherProvider,
) : MarketRepository {

    override suspend fun getQuote(symbol: String): Result<Quote> = withContext(dispatchers.io) {
        runCatching { api.getQuote(symbol).toDomain(symbol) }
    }

    override suspend fun getQuotes(symbols: List<String>): Result<List<Quote>> =
        withContext(dispatchers.io) {
            runCatching {
                coroutineScope {
                    symbols
                        .map { symbol -> async { api.getQuote(symbol).toDomain(symbol) } }
                        .awaitAll()
                }
            }
        }
}
