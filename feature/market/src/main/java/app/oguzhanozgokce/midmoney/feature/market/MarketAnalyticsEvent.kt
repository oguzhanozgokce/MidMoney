package app.oguzhanozgokce.midmoney.feature.market

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

sealed interface MarketAnalyticsEvent : AnalyticsEvent {

    data class OpenDetail(val symbol: String) : MarketAnalyticsEvent {
        override val name: String = "market_open_detail"
        override val params: Map<String, String> = mapOf("symbol" to symbol)
    }

    data object OpenWatchlist : MarketAnalyticsEvent {
        override val name: String = "market_open_watchlist"
    }

    data object Logout : MarketAnalyticsEvent {
        override val name: String = "logout"
    }
}
