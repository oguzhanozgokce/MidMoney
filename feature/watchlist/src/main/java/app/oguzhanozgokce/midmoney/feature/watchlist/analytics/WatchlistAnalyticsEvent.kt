package app.oguzhanozgokce.midmoney.feature.watchlist.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val WATCHLIST_VIEWED = "watchlist_viewed"
private const val WATCHLIST_OPEN_DETAIL = "watchlist_open_detail"
private const val PARAM_SYMBOL = "symbol"

sealed interface WatchlistAnalyticsEvent : AnalyticsEvent {

    data object Viewed : WatchlistAnalyticsEvent {
        override val name: String = WATCHLIST_VIEWED
    }

    data class OpenDetail(val symbol: String) : WatchlistAnalyticsEvent {
        override val name: String = WATCHLIST_OPEN_DETAIL
        override val params: Map<String, String> = mapOf(PARAM_SYMBOL to symbol)
    }
}
