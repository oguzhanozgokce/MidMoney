package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.feature.market.analytics.MarketAnalyticsEvent
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.toUi
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val marketClient: MarketClient,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel(),
    MVI<MarketUiState, MarketUiAction, MarketUiEffect> by mvi(MarketUiState()) {

    private var loadedQuotes: List<Quote> = emptyList()

    init {
        loadQuotes()
    }

    override fun onAction(uiAction: MarketUiAction) {
        when (uiAction) {
            is MarketUiAction.OpenDetail -> {
                analytics.track(MarketAnalyticsEvent.OpenDetail(uiAction.symbol))
                navigator.navigate(Destination.Detail(uiAction.symbol))
            }
            is MarketUiAction.SelectFilter -> {
                updateUiState { copy(selectedFilter = uiAction.filter, quotes = displayed(uiAction.filter)) }
            }
            MarketUiAction.Retry -> loadQuotes()
        }
    }

    private fun loadQuotes() {
        updateUiState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            marketClient.getQuotes()
                .onSuccess { quotes ->
                    loadedQuotes = quotes
                    updateUiState { copy(quotes = displayed(selectedFilter), isLoading = false) }
                }
                .onFailure { throwable ->
                    updateUiState {
                        copy(isLoading = false, errorMessage = throwable.message ?: "Something went wrong")
                    }
                }
        }
    }

    private fun displayed(filter: MarketFilter): List<QuoteUi> {
        val ordered = when (filter) {
            MarketFilter.Popular -> loadedQuotes
            MarketFilter.Gainers -> loadedQuotes.sortedByDescending { it.percentChange }
            MarketFilter.Losers -> loadedQuotes.sortedBy { it.percentChange }
        }
        return ordered.map { it.toUi() }
    }
}
