package app.oguzhanozgokce.midmoney.feature.market.presentation.list

import app.oguzhanozgokce.midmoney.feature.market.presentation.model.MarketFilter
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch

data class MarketListUiState(
    val query: String = "",
    val results: List<SymbolMatch> = emptyList(),
    val isSearching: Boolean = false,
    val quotes: List<QuoteUi> = emptyList(),
    val selectedFilter: MarketFilter = MarketFilter.Popular,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val isSearchActive: Boolean get() = query.isNotBlank()
}

sealed interface MarketListUiAction {
    data class OpenDetail(val symbol: String) : MarketListUiAction
    data class SelectFilter(val filter: MarketFilter) : MarketListUiAction
    data class QueryChanged(val query: String) : MarketListUiAction
    data object BackClicked : MarketListUiAction
    data object Retry : MarketListUiAction
}

sealed interface MarketListUiEffect
