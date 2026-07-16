package app.oguzhanozgokce.midmoney.feature.market.presentation

import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch

data class MarketsUiState(
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

sealed interface MarketsUiAction {
    data class OpenDetail(val symbol: String) : MarketsUiAction
    data class SelectFilter(val filter: MarketFilter) : MarketsUiAction
    data class QueryChanged(val query: String) : MarketsUiAction
    data object BackClicked : MarketsUiAction
    data object Retry : MarketsUiAction
}

sealed interface MarketsUiEffect
