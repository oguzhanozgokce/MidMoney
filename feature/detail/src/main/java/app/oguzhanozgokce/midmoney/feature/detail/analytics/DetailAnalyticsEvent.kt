package app.oguzhanozgokce.midmoney.feature.detail.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val DETAIL_VIEWED = "detail_viewed"
private const val DETAIL_BUY = "detail_buy"
private const val DETAIL_SELL = "detail_sell"
private const val DETAIL_SAVE = "detail_save"
private const val DETAIL_RETRY = "detail_retry"
private const val DETAIL_NEWS_OPENED = "detail_news_opened"
private const val DETAIL_PARAMS_SYMBOL = "symbol"

sealed interface DetailAnalyticsEvent : AnalyticsEvent {

    data class Viewed(val symbol: String) : DetailAnalyticsEvent {
        override val name: String = DETAIL_VIEWED
        override val params: Map<String, String> = mapOf(DETAIL_PARAMS_SYMBOL to symbol)
    }

    data class Buy(val symbol: String) : DetailAnalyticsEvent {
        override val name: String = DETAIL_BUY
        override val params: Map<String, String> = mapOf(DETAIL_PARAMS_SYMBOL to symbol)
    }

    data class Sell(val symbol: String) : DetailAnalyticsEvent {
        override val name: String = DETAIL_SELL
        override val params: Map<String, String> = mapOf(DETAIL_PARAMS_SYMBOL to symbol)
    }

    data class Save(val symbol: String, val saved: Boolean) : DetailAnalyticsEvent {
        override val name: String = DETAIL_SAVE
        override val params: Map<String, String> = mapOf(DETAIL_PARAMS_SYMBOL to symbol, "saved" to saved.toString())
    }

    data class Retry(val symbol: String) : DetailAnalyticsEvent {
        override val name: String = DETAIL_RETRY
        override val params: Map<String, String> = mapOf(DETAIL_PARAMS_SYMBOL to symbol)
    }

    data class NewsOpened(val symbol: String) : DetailAnalyticsEvent {
        override val name: String = DETAIL_NEWS_OPENED
        override val params: Map<String, String> = mapOf(DETAIL_PARAMS_SYMBOL to symbol)
    }
}
