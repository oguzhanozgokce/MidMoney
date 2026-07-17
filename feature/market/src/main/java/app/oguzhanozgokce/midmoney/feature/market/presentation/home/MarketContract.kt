package app.oguzhanozgokce.midmoney.feature.market.presentation.home

import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter
import app.oguzhanozgokce.midmoney.plugin.market.ui.QuoteUi

data class MarketUiState(
    val quotes: List<QuoteUi> = emptyList(),
    val selectedFilter: MarketFilter = MarketFilter.Popular,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
)

sealed interface MarketUiAction {
    data class OpenDetail(val symbol: String) : MarketUiAction
    data class SelectFilter(val filter: MarketFilter) : MarketUiAction
    data object OpenAll : MarketUiAction
    data object Retry : MarketUiAction
}

sealed interface MarketUiEffect
