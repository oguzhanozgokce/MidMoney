package app.oguzhanozgokce.midmoney.feature.market

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

data class MarketUiState(
    val quotes: List<Quote> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface MarketUiAction {
    data class OpenDetail(val symbol: String) : MarketUiAction
    data object OpenWatchlist : MarketUiAction
    data object Retry : MarketUiAction
}

sealed interface MarketUiEffect
