package app.oguzhanozgokce.midmoney.feature.market.presentation

import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi

data class MarketsUiState(
    val quotes: List<QuoteUi> = emptyList(),
    val selectedFilter: MarketFilter = MarketFilter.Popular,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface MarketsUiAction {
    data class OpenDetail(val symbol: String) : MarketsUiAction
    data class SelectFilter(val filter: MarketFilter) : MarketsUiAction
    data object BackClicked : MarketsUiAction
    data object Retry : MarketsUiAction
}

sealed interface MarketsUiEffect
