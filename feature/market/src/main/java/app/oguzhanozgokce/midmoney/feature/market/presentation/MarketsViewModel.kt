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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_MS = 350L

@HiltViewModel
class MarketsViewModel @Inject constructor(
    private val marketClient: MarketClient,
    private val navigator: Navigator,
) : ViewModel(),
    MVI<MarketsUiState, MarketsUiAction, MarketsUiEffect> by mvi(MarketsUiState()) {

    private var loadedQuotes: List<Quote> = emptyList()
    private var searchJob: Job? = null

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
            is MarketsUiAction.QueryChanged -> onQueryChanged(uiAction.query)
            MarketsUiAction.BackClicked -> navigator.goBack()
            MarketsUiAction.Retry -> loadQuotes()
        }
    }

    private fun onQueryChanged(query: String) {
        updateUiState { copy(query = query) }
        searchJob?.cancel()
        if (query.isBlank()) {
            updateUiState { copy(results = emptyList(), isSearching = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            updateUiState { copy(isSearching = true) }
            marketClient.search(query)
                .onSuccess { matches -> updateUiState { copy(results = matches, isSearching = false) } }
                .onFailure { updateUiState { copy(results = emptyList(), isSearching = false) } }
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
