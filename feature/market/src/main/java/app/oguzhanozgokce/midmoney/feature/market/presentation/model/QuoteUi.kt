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
        "AMD" to "Advanced Micro Devices",
        "INTC" to "Intel Corp.",
        "ORCL" to "Oracle Corp.",
        "CRM" to "Salesforce Inc.",
        "ADBE" to "Adobe Inc.",
        "CSCO" to "Cisco Systems Inc.",
        "QCOM" to "Qualcomm Inc.",
        "IBM" to "IBM Corp.",
        "UBER" to "Uber Technologies",
        "PYPL" to "PayPal Holdings",
        "DIS" to "The Walt Disney Co.",
        "KO" to "The Coca-Cola Co.",
        "PEP" to "PepsiCo Inc.",
        "NKE" to "Nike Inc.",
        "SBUX" to "Starbucks Corp.",
        "MCD" to "McDonald's Corp.",
        "JPM" to "JPMorgan Chase & Co.",
        "V" to "Visa Inc.",
        "MA" to "Mastercard Inc.",
        "WMT" to "Walmart Inc.",
    )

    fun of(symbol: String): String = names[symbol] ?: symbol
}
