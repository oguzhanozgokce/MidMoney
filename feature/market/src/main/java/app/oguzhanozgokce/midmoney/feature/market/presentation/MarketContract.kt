package app.oguzhanozgokce.midmoney.feature.market.presentation

import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi

data class MarketUiState(
    val quotes: List<QuoteUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface MarketUiAction {
    data class OpenDetail(val symbol: String) : MarketUiAction
    data object Retry : MarketUiAction
}

sealed interface MarketUiEffect
