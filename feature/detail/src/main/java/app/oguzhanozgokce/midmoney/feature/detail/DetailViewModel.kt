package app.oguzhanozgokce.midmoney.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val marketClient: MarketClient,
    private val navigator: Navigator,
) : ViewModel(),
    MVI<DetailUiState, DetailUiAction, DetailUiEffect> by mvi(DetailUiState()) {

    override fun onAction(uiAction: DetailUiAction) {
        when (uiAction) {
            is DetailUiAction.Load -> load(uiAction.symbol)
            DetailUiAction.BackClicked -> navigator.goBack()
        }
    }

    private fun load(symbol: String) {
        updateUiState { copy(symbol = symbol, isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            marketClient.getQuote(symbol)
                .onSuccess { quote ->
                    updateUiState { copy(quote = quote, isLoading = false) }
                }
                .onFailure { throwable ->
                    updateUiState {
                        copy(isLoading = false, errorMessage = throwable.message ?: "Something went wrong")
                    }
                }
        }
        observeLivePrice(symbol)
    }

    private fun observeLivePrice(symbol: String) {
        viewModelScope.launch {
            marketClient.observePrice(symbol)
                .catch { /* Ignore stream errors; the REST quote stays on screen. */ }
                .collect { price -> updateUiState { copy(livePrice = price) } }
        }
    }
}
