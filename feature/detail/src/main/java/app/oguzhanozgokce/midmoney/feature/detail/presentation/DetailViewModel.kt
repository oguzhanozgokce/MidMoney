package app.oguzhanozgokce.midmoney.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.common.extensions.formatPrice
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.error.errorMessageRes
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.feature.detail.R
import app.oguzhanozgokce.midmoney.feature.detail.analytics.DetailAnalyticsEvent
import app.oguzhanozgokce.midmoney.feature.detail.presentation.model.toDetailUi
import app.oguzhanozgokce.midmoney.feature.detail.presentation.model.toUi
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.domain.CompanyNames
import app.oguzhanozgokce.midmoney.plugin.news.NewsClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val marketClient: MarketClient,
    private val newsClient: NewsClient,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel(),
    MVI<DetailUiState, DetailUiAction, DetailUiEffect> by mvi(DetailUiState()) {

    override fun onAction(uiAction: DetailUiAction) {
        when (uiAction) {
            is DetailUiAction.Load -> load(uiAction.symbol)
            DetailUiAction.Retry -> load(currentUiState.symbol)
            DetailUiAction.BackClicked -> navigator.goBack()
            DetailUiAction.BuyClicked -> trade(isBuy = true)
            DetailUiAction.SellClicked -> trade(isBuy = false)
            DetailUiAction.ToggleSave -> toggleSave()
        }
    }

    private fun load(symbol: String) {
        analytics.track(DetailAnalyticsEvent.Viewed(symbol))
        updateUiState {
            copy(symbol = symbol, name = CompanyNames.of(symbol), isLoading = true, errorMessage = null)
        }
        viewModelScope.launch {
            marketClient.getQuote(symbol)
                .onSuccess { quote ->
                    updateUiState { copy(quote = quote.toDetailUi(), isLoading = false) }
                }
                .onFailure { throwable ->
                    updateUiState {
                        copy(isLoading = false, errorMessage = UiText.Resource(throwable.errorMessageRes()))
                    }
                }
        }
        observeLivePrice(symbol)
        observeSaved(symbol)
        loadNews(symbol)
    }

    private fun observeSaved(symbol: String) {
        viewModelScope.launch {
            marketClient.observeFavorite(symbol).collect { saved ->
                updateUiState { copy(isSaved = saved) }
            }
        }
    }

    private fun loadNews(symbol: String) {
        updateUiState { copy(isNewsLoading = true) }
        viewModelScope.launch {
            newsClient.getCompanyNews(symbol)
                .onSuccess { articles ->
                    updateUiState { copy(news = articles.map { it.toUi() }, isNewsLoading = false) }
                }
                .onFailure {
                    updateUiState { copy(news = emptyList(), isNewsLoading = false) }
                }
        }
    }

    private fun observeLivePrice(symbol: String) {
        viewModelScope.launch {
            marketClient.observePrice(symbol)
                .catch { /* Ignore stream errors; the REST quote stays on screen. */ }
                .collect { price -> updateUiState { copy(livePriceText = price.formatPrice()) } }
        }
    }

    private fun trade(isBuy: Boolean) {
        val state = currentUiState
        val name = state.name.ifBlank { state.symbol }
        val event = if (isBuy) DetailAnalyticsEvent.Buy(state.symbol) else DetailAnalyticsEvent.Sell(state.symbol)
        analytics.track(event)
        val messageRes = if (isBuy) R.string.detail_redirect_buy else R.string.detail_redirect_sell
        viewModelScope.launch {
            emitUiEffect(DetailUiEffect.ShowMessage(UiText.Resource(messageRes, name)))
        }
    }

    private fun toggleSave() {
        val symbol = currentUiState.symbol
        if (symbol.isBlank()) return
        val willSave = !currentUiState.isSaved
        analytics.track(DetailAnalyticsEvent.Save(symbol, willSave))
        val messageRes = if (willSave) R.string.detail_saved else R.string.detail_unsaved
        viewModelScope.launch {
            marketClient.toggleFavorite(symbol)
            emitUiEffect(DetailUiEffect.ShowMessage(UiText.Resource(messageRes)))
        }
    }
}
