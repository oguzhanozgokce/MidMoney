package app.oguzhanozgokce.midmoney.feature.market.presentation

import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi

data class MarketUiState(
    val quotes: List<QuoteUi> = emptyList(),
    val selectedFilter: MarketFilter = MarketFilter.Popular,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

enum class MarketFilter(val label: String) {
    Popular("Popular"),
    Gainers("Gainers"),
    Losers("Losers"),
}

sealed interface MarketUiAction {
    data class OpenDetail(val symbol: String) : MarketUiAction
    data class SelectFilter(val filter: MarketFilter) : MarketUiAction
    data object OpenAll : MarketUiAction
    data object Retry : MarketUiAction
}

sealed interface MarketUiEffect
