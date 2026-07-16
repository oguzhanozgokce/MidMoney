package app.oguzhanozgokce.midmoney.plugin.market

import app.oguzhanozgokce.midmoney.plugin.market.domain.MarketSymbols
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketClient @Inject constructor(
    private val repository: MarketRepository,
) {
    suspend fun getQuotes(): Result<List<Quote>> = repository.getQuotes(MarketSymbols.home)
    suspend fun getAllQuotes(): Result<List<Quote>> = repository.getQuotes(MarketSymbols.all)
    suspend fun getQuote(symbol: String): Result<Quote> = repository.getQuote(symbol)
    suspend fun search(query: String): Result<List<SymbolMatch>> = repository.searchSymbols(query)
    fun observePrice(symbol: String): Flow<Double> = repository.observePrice(symbol)
}
