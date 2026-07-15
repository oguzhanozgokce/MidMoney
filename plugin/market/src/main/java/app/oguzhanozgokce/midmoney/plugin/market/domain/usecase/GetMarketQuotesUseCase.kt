package app.oguzhanozgokce.midmoney.plugin.market.domain.usecase

import app.oguzhanozgokce.midmoney.plugin.market.domain.MarketSymbols
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import javax.inject.Inject

class GetMarketQuotesUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    suspend operator fun invoke(): Result<List<Quote>> =
        repository.getQuotes(MarketSymbols.default)
}
