package app.oguzhanozgokce.midmoney.feature.market

data class MarketUiState(
    val isLoading: Boolean = false,
)

sealed interface MarketUiAction {
    data class OpenDetail(val symbol: String) : MarketUiAction
    data object OpenWatchlist : MarketUiAction
}

sealed interface MarketUiEffect
