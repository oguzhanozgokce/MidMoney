package app.oguzhanozgokce.midmoney.feature.market.presentation

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter

object MarketTestTags {
    const val SCREEN = "market.screen"
    const val BANNER = "market.banner"
    const val SEE_ALL = "market.see_all"
    const val LOADING = "market.loading"
    const val RETRY = "market.retry"

    fun filter(filter: MarketFilter): String = "market.filter." + filter.name.lowercase()

    fun quote(symbol: String): String = "market.quote.$symbol"
}
