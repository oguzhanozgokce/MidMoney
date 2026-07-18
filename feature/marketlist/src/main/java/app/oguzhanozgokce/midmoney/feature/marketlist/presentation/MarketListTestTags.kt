package app.oguzhanozgokce.midmoney.feature.marketlist.presentation

object MarketListTestTags {
    const val SCREEN = "marketlist.screen"
    const val SEARCH = "marketlist.search"
    const val RETRY = "marketlist.retry"

    fun quote(symbol: String): String = "marketlist.quote.$symbol"
}
