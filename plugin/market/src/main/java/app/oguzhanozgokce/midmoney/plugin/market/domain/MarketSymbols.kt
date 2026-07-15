package app.oguzhanozgokce.midmoney.plugin.market.domain

/**
 * The default set of symbols shown on the market screen. Finnhub's free tier does not offer a bulk
 * quote endpoint, so the repository fetches these individually.
 */
object MarketSymbols {
    val default: List<String> = listOf(
        "AAPL",
        "GOOGL",
        "MSFT",
        "AMZN",
        "TSLA",
        "META",
        "NVDA",
        "NFLX",
    )
}
