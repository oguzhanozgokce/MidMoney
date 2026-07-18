package app.oguzhanozgokce.midmoney.feature.marketlist.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val MARKETLIST_VIEWED = "marketlist_viewed"
private const val MARKETLIST_SEARCHED = "marketlist_searched"
private const val MARKETLIST_OPEN_DETAIL = "marketlist_open_detail"
private const val MARKETLIST_FILTER_SELECTED = "marketlist_filter_selected"
private const val PARAM_SYMBOL = "symbol"
private const val PARAM_FILTER = "filter"
private const val PARAM_QUERY = "query"

sealed interface MarketListAnalyticsEvent : AnalyticsEvent {

    data object Viewed : MarketListAnalyticsEvent {
        override val name: String = MARKETLIST_VIEWED
    }

    data class Searched(val query: String) : MarketListAnalyticsEvent {
        override val name: String = MARKETLIST_SEARCHED
        override val params: Map<String, String> = mapOf(PARAM_QUERY to query)
    }

    data class OpenDetail(val symbol: String) : MarketListAnalyticsEvent {
        override val name: String = MARKETLIST_OPEN_DETAIL
        override val params: Map<String, String> = mapOf(PARAM_SYMBOL to symbol)
    }

    data class FilterSelected(val filter: String) : MarketListAnalyticsEvent {
        override val name: String = MARKETLIST_FILTER_SELECTED
        override val params: Map<String, String> = mapOf(PARAM_FILTER to filter)
    }
}
