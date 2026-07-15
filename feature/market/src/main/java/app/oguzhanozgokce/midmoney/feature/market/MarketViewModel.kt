package app.oguzhanozgokce.midmoney.feature.market

import androidx.lifecycle.ViewModel
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel(),
    MVI<MarketUiState, MarketUiAction, MarketUiEffect> by mvi(MarketUiState()) {

    override fun onAction(uiAction: MarketUiAction) {
        when (uiAction) {
            is MarketUiAction.OpenDetail -> navigator.navigate(Destination.Detail(uiAction.symbol))
            MarketUiAction.OpenWatchlist -> navigator.navigate(Destination.Watchlist)
        }
    }
}
