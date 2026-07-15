package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.feature.market.analytics.MarketAnalyticsEvent
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.toUi
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.user.UserClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val marketClient: MarketClient,
    private val userClient: UserClient,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel(),
    MVI<MarketUiState, MarketUiAction, MarketUiEffect> by mvi(MarketUiState()) {

    init {
        loadQuotes()
    }

    override fun onAction(uiAction: MarketUiAction) {
        when (uiAction) {
            is MarketUiAction.OpenDetail -> {
                analytics.track(MarketAnalyticsEvent.OpenDetail(uiAction.symbol))
                navigator.navigate(Destination.Detail(uiAction.symbol))
            }
            MarketUiAction.OpenWatchlist -> {
                analytics.track(MarketAnalyticsEvent.OpenWatchlist)
                navigator.navigate(Destination.Watchlist)
            }
            MarketUiAction.Retry -> loadQuotes()
            MarketUiAction.Logout -> logout()
        }
    }

    private fun logout() {
        analytics.track(MarketAnalyticsEvent.Logout)
        userClient.logout()
        navigator.navigateAndClearBackStack(Destination.Login)
    }

    private fun loadQuotes() {
        updateUiState { copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            marketClient.getQuotes()
                .onSuccess { quotes ->
                    updateUiState { copy(quotes = quotes.map { it.toUi() }, isLoading = false) }
                }
                .onFailure { throwable ->
                    updateUiState {
                        copy(isLoading = false, errorMessage = throwable.message ?: "Something went wrong")
                    }
                }
        }
    }
}
