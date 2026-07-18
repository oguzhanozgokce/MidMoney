package app.oguzhanozgokce.midmoney.feature.market.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val MARKET_VIEWED = "market_viewed"
private const val MARKET_OPEN_DETAIL = "market_open_detail"
private const val MARKET_FILTER_SELECTED = "market_filter_selected"
private const val MARKET_SEE_ALL = "market_see_all"
private const val PARAM_SYMBOL = "symbol"
private const val PARAM_FILTER = "filter"

sealed interface MarketAnalyticsEvent : AnalyticsEvent {

    data object Viewed : MarketAnalyticsEvent {
        override val name: String = MARKET_VIEWED
    }

    data class OpenDetail(val symbol: String) : MarketAnalyticsEvent {
        override val name: String = MARKET_OPEN_DETAIL
        override val params: Map<String, String> = mapOf(PARAM_SYMBOL to symbol)
    }

    data class FilterSelected(val filter: String) : MarketAnalyticsEvent {
        override val name: String = MARKET_FILTER_SELECTED
        override val params: Map<String, String> = mapOf(PARAM_FILTER to filter)
    }

    data object SeeAllClicked : MarketAnalyticsEvent {
        override val name: String = MARKET_SEE_ALL
    }
}
