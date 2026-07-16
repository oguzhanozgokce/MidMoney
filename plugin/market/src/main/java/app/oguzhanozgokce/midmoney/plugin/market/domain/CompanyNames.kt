package app.oguzhanozgokce.midmoney.plugin.market.domain

/**
 * Display names for our curated symbols. Finnhub's /quote returns prices only, so names are mapped
 * locally; unknown symbols fall back to the ticker.
 */
object CompanyNames {
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
