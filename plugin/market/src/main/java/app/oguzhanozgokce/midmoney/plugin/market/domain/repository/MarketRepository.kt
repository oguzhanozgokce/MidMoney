package app.oguzhanozgokce.midmoney.plugin.market.domain.repository

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

interface MarketRepository {
    suspend fun getQuotes(symbols: List<String>): Result<List<Quote>>
    suspend fun getQuote(symbol: String): Result<Quote>
}
