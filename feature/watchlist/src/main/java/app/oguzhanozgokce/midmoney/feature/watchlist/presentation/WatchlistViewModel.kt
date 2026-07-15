package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

import androidx.lifecycle.ViewModel
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.feature.watchlist.analytics.WatchlistAnalyticsEvent
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val analytics: Analytics,
) : ViewModel(),
    MVI<WatchlistUiState, WatchlistUiAction, WatchlistUiEffect> by mvi(WatchlistUiState()) {

    init {
        analytics.track(WatchlistAnalyticsEvent.Viewed)
    }
}
