package app.oguzhanozgokce.midmoney.feature.watchlist

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

sealed interface WatchlistAnalyticsEvent : AnalyticsEvent {

    data object Viewed : WatchlistAnalyticsEvent {
        override val name: String = "watchlist_viewed"
    }
}
