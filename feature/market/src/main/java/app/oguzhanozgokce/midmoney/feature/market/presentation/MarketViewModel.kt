package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.error.errorMessageRes
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.event.EventSupplier
import app.oguzhanozgokce.midmoney.feature.market.analytics.MarketAnalyticsEvent
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.domain.applyFilter
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.ui.QuoteUi
import app.oguzhanozgokce.midmoney.plugin.market.ui.toUi
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
                analytics.track(MarketAnalyticsEvent.OpenDetail(uiAction.symbol), EventSupplier.All)
                navigator.navigate(Destination.Detail(uiAction.symbol))
            }
            is MarketUiAction.SelectFilter -> {
                updateUiState {
                    copy(selectedFilter = uiAction.filter, quotes = displayed(uiAction.filter))
                }
            }
            MarketUiAction.OpenAll -> navigator.navigate(Destination.MarketList)
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
                        copy(isLoading = false, errorMessage = UiText.Resource(throwable.errorMessageRes()))
                    }
                }
        }
    }

    private fun displayed(filter: MarketFilter): List<QuoteUi> =
        loadedQuotes.applyFilter(filter).map { it.toUi() }
}
