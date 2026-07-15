package app.oguzhanozgokce.midmoney.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.domain.usecase.GetQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getQuote: GetQuoteUseCase,
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
            getQuote(symbol)
                .onSuccess { quote ->
                    updateUiState { copy(quote = quote, isLoading = false) }
                }
                .onFailure { throwable ->
                    updateUiState {
                        copy(isLoading = false, errorMessage = throwable.message ?: "Something went wrong")
                    }
                }
        }
    }
}
