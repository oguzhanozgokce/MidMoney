package app.oguzhanozgokce.midmoney.feature.watchlist

data class WatchlistUiState(
    val isLoading: Boolean = false,
)

sealed interface WatchlistUiAction {
    data object BackClicked : WatchlistUiAction
}

sealed interface WatchlistUiEffect
