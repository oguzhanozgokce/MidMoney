package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

object WatchlistTestTags {
    const val SCREEN = "watchlist.screen"
    const val EMPTY = "watchlist.empty"
    const val RETRY = "watchlist.retry"

    fun quote(symbol: String): String = "watchlist.quote.$symbol"
}
