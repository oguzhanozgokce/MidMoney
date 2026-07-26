package app.oguzhanozgokce.midmoney.plugin.market.data.repository

import app.oguzhanozgokce.midmoney.error.ErrorHandler
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.FinnhubApi
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.FinnhubTradeStream
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.toDomain
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MarketRepositoryImpl @Inject constructor(
    private val api: FinnhubApi,
    private val tradeStream: FinnhubTradeStream,
    private val errorHandler: ErrorHandler,
) : MarketRepository {

    override suspend fun getQuote(symbol: String): Result<Quote> =
        errorHandler.call { api.getQuote(symbol).toDomain(symbol) }

    override suspend fun searchSymbols(query: String): Result<List<SymbolMatch>> =
        errorHandler.call { api.search(query).toDomain() }

    override suspend fun getQuotes(symbols: List<String>): Result<List<Quote>> =
        errorHandler.call {
            coroutineScope {
                symbols
                    .map { symbol -> async { api.getQuote(symbol).toDomain(symbol) } }
                    .awaitAll()
            }
        }

    override fun observePrice(symbol: String): Flow<Double> =
        tradeStream.observePrices(listOf(symbol))
            .filter { it.symbol == symbol }
            .map { it.price }
}
