package app.oguzhanozgokce.midmoney.plugin.market.domain.repository

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    suspend fun getQuotes(symbols: List<String>): Result<List<Quote>>
    suspend fun getQuote(symbol: String): Result<Quote>

    /** Live last price for [symbol] from the WebSocket stream. */
    fun observePrice(symbol: String): Flow<Double>
}
