package app.oguzhanozgokce.midmoney.feature.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.domain.usecase.GetMarketQuotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getMarketQuotes: GetMarketQuotesUseCase,
    private val navigator: Navigator,
) : ViewModel(),
    MVI<MarketUiState, MarketUiAction, MarketUiEffect> by mvi(MarketUiState()) {

    init {
        loadQuotes()
    }

    override fun onAction(uiAction: MarketUiAction) {
        when (uiAction) {
            is MarketUiAction.OpenDetail -> navigator.navigate(Destination.Detail(uiAction.symbol))
            MarketUiAction.OpenWatchlist -> navigator.navigate(Destination.Watchlist)
            MarketUiAction.Retry -> loadQuotes()
        }
    }

    private fun loadQuotes() {
        updateUiState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getMarketQuotes()
                .onSuccess { quotes ->
                    updateUiState { copy(quotes = quotes, isLoading = false) }
                }
                .onFailure { throwable ->
                    updateUiState {
                        copy(isLoading = false, errorMessage = throwable.message ?: "Something went wrong")
                    }
                }
        }
    }
}
