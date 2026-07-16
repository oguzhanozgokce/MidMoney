package app.oguzhanozgokce.midmoney.plugin.market.domain

object MarketSymbols {
    val all: List<String> = listOf(
        "AAPL",
        "MSFT",
        "GOOGL",
        "AMZN",
        "NVDA",
        "META",
        "TSLA",
        "NFLX",
        "AMD",
        "INTC",
        "ORCL",
        "CRM",
        "ADBE",
        "CSCO",
        "QCOM",
        "IBM",
        "UBER",
        "PYPL",
        "DIS",
        "KO",
        "PEP",
        "NKE",
        "SBUX",
        "MCD",
        "JPM",
        "V",
        "MA",
        "WMT",
    )

    val home: List<String> = all.take(HOME_COUNT)

    private const val HOME_COUNT = 8
}
