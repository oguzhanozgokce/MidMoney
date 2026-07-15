package app.oguzhanozgokce.midmoney.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.feature.profile.analytics.ProfileAnalyticsEvent
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.user.UserClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userClient: UserClient,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel(),
    MVI<ProfileUiState, ProfileUiAction, ProfileUiEffect> by mvi(ProfileUiState()) {

    init {
        analytics.track(ProfileAnalyticsEvent.Viewed)
        observeEmail()
    }

    override fun onAction(uiAction: ProfileUiAction) {
        when (uiAction) {
            ProfileUiAction.Logout -> logout()
        }
    }

    private fun observeEmail() {
        viewModelScope.launch {
            userClient.currentUserEmail.collect { email -> updateUiState { copy(email = email) } }
        }
    }

    private fun logout() {
        analytics.track(ProfileAnalyticsEvent.Logout)
        userClient.logout()
        navigator.navigateAndClearBackStack(Destination.Login)
    }
}
