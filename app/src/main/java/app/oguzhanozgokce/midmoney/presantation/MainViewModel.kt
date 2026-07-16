package app.oguzhanozgokce.midmoney.presantation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.plugin.user.UserClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userClient: UserClient,
    private val analytics: Analytics,
) : ViewModel(), MVI<MainUiState, MainUiAction, MainUiEffect> by mvi(MainUiState()) {

    init {
        resolveStartDestination()
        syncAnalyticsUserId()
    }

    private fun resolveStartDestination() {
        val startDestination = if (userClient.isCurrentlyLoggedIn()) {
            Destination.Home
        } else {
            Destination.Login
        }
        updateUiState { copy(startDestination = startDestination) }
    }

    // Keep the analytics user id in sync with the auth state (set on login/session, cleared on logout).
    private fun syncAnalyticsUserId() {
        viewModelScope.launch {
            userClient.currentUserId.collect { userId -> analytics.setUserId(userId) }
        }
    }
}
