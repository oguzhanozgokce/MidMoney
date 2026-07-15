package app.oguzhanozgokce.midmoney.plugin.market

import app.oguzhanozgokce.midmoney.plugin.market.domain.MarketSymbols
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Public entry point of the market plugin. Features depend on this client instead of the repository
 * (which stays internal to the plugin), keeping a single, cohesive API surface.
 */
class MarketClient @Inject constructor(
    private val repository: MarketRepository,
) {
    suspend fun getQuotes(): Result<List<Quote>> = repository.getQuotes(MarketSymbols.default)
    suspend fun getQuote(symbol: String): Result<Quote> = repository.getQuote(symbol)
    fun observePrice(symbol: String): Flow<Double> = repository.observePrice(symbol)
}
