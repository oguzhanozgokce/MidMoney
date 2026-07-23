package app.oguzhanozgokce.midmoney.feature.marketlist.presentation

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter

object MarketListTestTags {
    const val SCREEN = "marketlist.screen"
    const val SEARCH = "marketlist.search"
    const val EMPTY = "marketlist.empty"
    const val RETRY = "marketlist.retry"

    fun filter(filter: MarketFilter): String = "marketlist.filter." + filter.name.lowercase()
    fun quote(symbol: String): String = "marketlist.quote.$symbol"
    fun result(symbol: String): String = "marketlist.result.$symbol"
}
