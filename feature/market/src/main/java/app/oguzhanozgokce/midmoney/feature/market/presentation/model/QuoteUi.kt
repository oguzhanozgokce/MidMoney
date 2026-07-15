package app.oguzhanozgokce.midmoney.feature.market.presentation.model

import app.oguzhanozgokce.midmoney.common.extensions.formatPrice
import app.oguzhanozgokce.midmoney.common.extensions.formatSignedPercent
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

data class QuoteUi(
    val symbol: String,
    val name: String,
    val priceText: String,
    val changePercentText: String,
    val isPositive: Boolean,
)

fun Quote.toUi(): QuoteUi = QuoteUi(
    symbol = symbol,
    name = CompanyNames.of(symbol),
    priceText = current.formatPrice(),
    changePercentText = percentChange.formatSignedPercent(),
    isPositive = percentChange >= 0,
)

/**
 * Finnhub's quote endpoint returns prices only, so we map our curated symbols to display names
 * locally. Unknown symbols fall back to the ticker itself.
 */
private object CompanyNames {
    private val names = mapOf(
        "AAPL" to "Apple Inc.",
        "GOOGL" to "Alphabet Inc.",
        "MSFT" to "Microsoft Corp.",
        "AMZN" to "Amazon.com Inc.",
        "TSLA" to "Tesla Inc.",
        "META" to "Meta Platforms Inc.",
        "NVDA" to "NVIDIA Corp.",
        "NFLX" to "Netflix Inc.",
    )

    fun of(symbol: String): String = names[symbol] ?: symbol
}
