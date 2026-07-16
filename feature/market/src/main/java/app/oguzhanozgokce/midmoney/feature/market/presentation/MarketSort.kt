package app.oguzhanozgokce.midmoney.feature.market.presentation

import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.toUi
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

internal fun List<Quote>.toDisplayList(filter: MarketFilter): List<QuoteUi> {
    val ordered = when (filter) {
        MarketFilter.Popular -> this
        MarketFilter.Gainers -> sortedByDescending { it.percentChange }
        MarketFilter.Losers -> sortedBy { it.percentChange }
    }
    return ordered.map { it.toUi() }
}
