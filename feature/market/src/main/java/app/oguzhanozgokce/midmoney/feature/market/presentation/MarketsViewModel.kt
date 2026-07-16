package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MarketsViewModel @Inject constructor(
    private val marketClient: MarketClient,
    private val navigator: Navigator,
) : ViewModel(),
    MVI<MarketsUiState, MarketsUiAction, MarketsUiEffect> by mvi(MarketsUiState()) {

    private var loadedQuotes: List<Quote> = emptyList()

    init {
        loadQuotes()
    }

    override fun onAction(uiAction: MarketsUiAction) {
        when (uiAction) {
            is MarketsUiAction.OpenDetail -> navigator.navigate(Destination.Detail(uiAction.symbol))
            is MarketsUiAction.SelectFilter -> {
                updateUiState {
                    copy(selectedFilter = uiAction.filter, quotes = loadedQuotes.toDisplayList(uiAction.filter))
                }
            }
            MarketsUiAction.BackClicked -> navigator.goBack()
            MarketsUiAction.Retry -> loadQuotes()
        }
    }

    private fun loadQuotes() {
        updateUiState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            marketClient.getAllQuotes()
                .onSuccess { quotes ->
                    loadedQuotes = quotes
                    updateUiState { copy(quotes = quotes.toDisplayList(selectedFilter), isLoading = false) }
                }
                .onFailure { throwable ->
                    updateUiState {
                        copy(isLoading = false, errorMessage = throwable.message ?: "Something went wrong")
                    }
                }
        }
    }
}
