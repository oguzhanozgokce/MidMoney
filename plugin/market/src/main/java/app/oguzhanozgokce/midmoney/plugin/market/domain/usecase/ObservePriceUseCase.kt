package app.oguzhanozgokce.midmoney.plugin.market.domain.usecase

import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePriceUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    operator fun invoke(symbol: String): Flow<Double> = repository.observePrice(symbol)
}
