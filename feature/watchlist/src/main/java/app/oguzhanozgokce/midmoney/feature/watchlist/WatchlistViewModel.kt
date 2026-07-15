package app.oguzhanozgokce.midmoney.feature.watchlist

import androidx.lifecycle.ViewModel
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel(),
    MVI<WatchlistUiState, WatchlistUiAction, WatchlistUiEffect> by mvi(WatchlistUiState()) {

    override fun onAction(uiAction: WatchlistUiAction) {
        when (uiAction) {
            WatchlistUiAction.BackClicked -> navigator.goBack()
        }
    }
}
