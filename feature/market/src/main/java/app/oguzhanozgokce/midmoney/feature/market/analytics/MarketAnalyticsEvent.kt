package app.oguzhanozgokce.midmoney.feature.market.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val MARKET_OPEN_DETAIL = "market_open_detail"
private const val MARKET_OPEN_DETAIL_PARAM_SYMBOL = "symbol"

sealed interface MarketAnalyticsEvent : AnalyticsEvent {

    data class OpenDetail(val symbol: String) : MarketAnalyticsEvent {
        override val name: String = MARKET_OPEN_DETAIL
        override val params: Map<String, String> = mapOf(MARKET_OPEN_DETAIL_PARAM_SYMBOL to symbol)
    }
}
