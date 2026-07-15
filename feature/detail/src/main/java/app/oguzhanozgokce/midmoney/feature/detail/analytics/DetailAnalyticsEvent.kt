package app.oguzhanozgokce.midmoney.feature.detail.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

sealed interface DetailAnalyticsEvent : AnalyticsEvent {

    data class Viewed(val symbol: String) : DetailAnalyticsEvent {
        override val name: String = "detail_viewed"
        override val params: Map<String, String> = mapOf("symbol" to symbol)
    }
}
