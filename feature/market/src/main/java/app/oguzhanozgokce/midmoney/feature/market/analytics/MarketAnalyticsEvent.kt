package app.oguzhanozgokce.midmoney.feature.market.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val MARKET_OPEN_DETAIL = "market_open_detail"
private const val MARKET_OPEN_WATCHLIST = "market_open_watchlist"
private const val MARKET_LOGOUT = "market_logout"

sealed interface MarketAnalyticsEvent : AnalyticsEvent {

    data class OpenDetail(val symbol: String) : MarketAnalyticsEvent {
        override val name: String = MARKET_OPEN_DETAIL
        override val params: Map<String, String> = mapOf("symbol" to symbol)
    }

    data object OpenWatchlist : MarketAnalyticsEvent {
        override val name: String = MARKET_OPEN_WATCHLIST
    }

    data object Logout : MarketAnalyticsEvent {
        override val name: String = MARKET_LOGOUT
    }
}
