package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.error.errorMessageRes
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.event.EventSupplier
import app.oguzhanozgokce.midmoney.feature.market.analytics.MarketAnalyticsEvent
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.MarketBannerUi
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.toUi
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.domain.applyFilter
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.remoteconfig.RemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val marketClient: MarketClient,
    private val navigator: Navigator,
    private val analytics: Analytics,
    private val remoteConfig: RemoteConfig,
) : ViewModel(),
    MVI<MarketUiState, MarketUiAction, MarketUiEffect> by mvi(MarketUiState()) {

    private var loadedQuotes: List<Quote> = emptyList()

    init {
        analytics.track(MarketAnalyticsEvent.Viewed, EventSupplier.All)
        updateUiState { copy(banners = visibleBanners()) }
        refreshBanners()
        loadQuotes()
    }

    private fun refreshBanners() {
        viewModelScope.launch {
            remoteConfig.activate()
            updateUiState { copy(banners = visibleBanners()) }
        }
    }

    private fun visibleBanners(): List<MarketBannerUi> =
        MarketBannerUi.defaults.filter { remoteConfig.getBoolean(it.remoteConfigKey, default = true) }

    override fun onAction(uiAction: MarketUiAction) {
        when (uiAction) {
            is MarketUiAction.OpenDetail -> openDetailHandle(uiAction.symbol)
            is MarketUiAction.SelectFilter -> selectFilterHandle(uiAction.filter)
            MarketUiAction.OpenAll -> openAllHandle()
            MarketUiAction.Retry -> loadQuotes()
        }
    }

    private fun openDetailHandle(symbol: String) {
        analytics.track(MarketAnalyticsEvent.OpenDetail(symbol), EventSupplier.All)
        navigator.navigate(Destination.Detail(symbol))
    }

    private fun selectFilterHandle(filter: MarketFilter) {
        analytics.track(MarketAnalyticsEvent.FilterSelected(filter.name), EventSupplier.All)
        updateUiState { copy(selectedFilter = filter, quotes = displayed(filter)) }
    }

    private fun openAllHandle() {
        analytics.track(MarketAnalyticsEvent.SeeAllClicked, EventSupplier.All)
        navigator.navigate(Destination.MarketList)
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
