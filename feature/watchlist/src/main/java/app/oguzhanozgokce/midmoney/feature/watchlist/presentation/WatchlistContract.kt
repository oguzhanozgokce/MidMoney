package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

data class WatchlistUiState(
    val isLoading: Boolean = false,
)

sealed interface WatchlistUiAction

sealed interface WatchlistUiEffect
