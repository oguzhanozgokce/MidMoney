package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.error.errorMessageRes
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.event.EventSupplier
import app.oguzhanozgokce.midmoney.feature.watchlist.analytics.WatchlistAnalyticsEvent
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.ui.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val marketClient: MarketClient,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel(),
    MVI<WatchlistUiState, WatchlistUiAction, WatchlistUiEffect> by mvi(WatchlistUiState()) {

    private var symbols: List<String> = emptyList()

    init {
        analytics.track(WatchlistAnalyticsEvent.Viewed, EventSupplier.All)
        observeFavorites()
    }

    override fun onAction(uiAction: WatchlistUiAction) {
        when (uiAction) {
            is WatchlistUiAction.OpenDetail -> navigator.navigate(Destination.Detail(uiAction.symbol))
            WatchlistUiAction.Retry -> fetchQuotes()
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            marketClient.favorites.collect { favorites ->
                symbols = favorites.toList()
                fetchQuotes()
            }
        }
    }

    private fun fetchQuotes() {
        val current = symbols
        if (current.isEmpty()) {
            updateUiState { copy(items = emptyList(), isLoading = false, errorMessage = null) }
            return
        }
        updateUiState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            marketClient.getQuotes(current)
                .onSuccess { quotes ->
                    updateUiState { copy(items = quotes.map { it.toUi() }, isLoading = false) }
                }
                .onFailure { throwable ->
                    updateUiState {
                        copy(isLoading = false, errorMessage = UiText.Resource(throwable.errorMessageRes()))
                    }
                }
        }
    }
}
