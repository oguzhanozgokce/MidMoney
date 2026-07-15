package app.oguzhanozgokce.midmoney

import androidx.lifecycle.ViewModel
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.plugin.user.UserClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userClient: UserClient,
) : ViewModel(), MVI<MainUiState, MainUiAction, MainUiEffect> by mvi(MainUiState()) {

    init {
        startDestination()
    }

    private fun startDestination() {
        val startDestination = if (userClient.isCurrentlyLoggedIn()) {
            Destination.Market
        } else {
            Destination.Login
        }
        updateUiState { copy(startDestination = startDestination) }
    }
}
