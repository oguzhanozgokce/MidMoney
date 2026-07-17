package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.plugin.market.ui.QuoteUi

data class WatchlistUiState(
    val items: List<QuoteUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
) {
    val isEmpty: Boolean get() = !isLoading && errorMessage == null && items.isEmpty()
}

sealed interface WatchlistUiAction {
    data class OpenDetail(val symbol: String) : WatchlistUiAction
    data object Retry : WatchlistUiAction
}

sealed interface WatchlistUiEffect
