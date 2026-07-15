package app.oguzhanozgokce.midmoney.plugin.market.domain.usecase

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import javax.inject.Inject

class GetQuoteUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    suspend operator fun invoke(symbol: String): Result<Quote> =
        repository.getQuote(symbol)
}
