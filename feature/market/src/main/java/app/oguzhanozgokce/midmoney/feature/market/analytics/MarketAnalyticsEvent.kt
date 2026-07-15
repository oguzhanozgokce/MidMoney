package app.oguzhanozgokce.midmoney.feature.market.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val MARKET_OPEN_DETAIL = "market_open_detail"

sealed interface MarketAnalyticsEvent : AnalyticsEvent {

    data class OpenDetail(val symbol: String) : MarketAnalyticsEvent {
        override val name: String = MARKET_OPEN_DETAIL
        override val params: Map<String, String> = mapOf("symbol" to symbol)
    }
}
