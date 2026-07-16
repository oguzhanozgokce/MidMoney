package app.oguzhanozgokce.midmoney.feature.market.presentation.model

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

/** Orders quotes for the selected filter and maps them to UI models. Shared by the home and list screens. */
internal fun List<Quote>.toDisplayList(filter: MarketFilter): List<QuoteUi> {
    val ordered = when (filter) {
        MarketFilter.Popular -> this
        MarketFilter.Gainers -> sortedByDescending { it.percentChange }
        MarketFilter.Losers -> sortedBy { it.percentChange }
    }
    return ordered.map { it.toUi() }
}
