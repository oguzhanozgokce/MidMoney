package app.oguzhanozgokce.midmoney.plugin.market.domain

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

/** Orders quotes for the selected filter. Shared by the market and market-list screens. */
fun List<Quote>.applyFilter(filter: MarketFilter): List<Quote> = when (filter) {
    MarketFilter.Popular -> this
    MarketFilter.Gainers -> sortedByDescending { it.percentChange }
    MarketFilter.Losers -> sortedBy { it.percentChange }
}
