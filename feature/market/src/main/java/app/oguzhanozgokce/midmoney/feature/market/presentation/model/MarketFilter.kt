package app.oguzhanozgokce.midmoney.feature.market.presentation.model

/**
 * Client-side views over the curated symbol list — the free Finnhub tier has no trending endpoint,
 * so "Popular" is the curated order and gainers/losers sort by daily change.
 */
enum class MarketFilter(val label: String) {
    Popular("Popular"),
    Gainers("Gainers"),
    Losers("Losers"),
}
