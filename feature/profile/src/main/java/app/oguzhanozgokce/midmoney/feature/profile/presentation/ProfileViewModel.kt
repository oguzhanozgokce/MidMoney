package app.oguzhanozgokce.midmoney.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.common.appinfo.AppInfoProvider
import app.oguzhanozgokce.midmoney.common.config.AppConfig
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.event.EventSupplier
import app.oguzhanozgokce.midmoney.feature.profile.R
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
    appInfo: AppInfoProvider,
    appConfig: AppConfig,
) : ViewModel(),
    MVI<ProfileUiState, ProfileUiAction, ProfileUiEffect> by mvi(ProfileUiState()) {

    init {
        analytics.track(ProfileAnalyticsEvent.Viewed, EventSupplier.All)
        updateUiState {
            copy(
                versionName = appInfo.versionName,
                environmentLabel = appConfig.environment.uppercase().takeIf { appConfig.isPreprod },
            )
        }
        observeEmail()
    }

    override fun onAction(uiAction: ProfileUiAction) {
        when (uiAction) {
            ProfileUiAction.Logout -> logout()
            ProfileUiAction.ComingSoonClicked -> showComingSoon()
        }
    }

    private fun showComingSoon() {
        viewModelScope.launch {
            emitUiEffect(ProfileUiEffect.ShowMessage(UiText.Resource(R.string.profile_coming_soon)))
        }
    }

    private fun observeEmail() {
        viewModelScope.launch {
            userClient.currentUserEmail.collect { email -> updateUiState { copy(email = email) } }
        }
    }

    private fun logout() {
        analytics.track(ProfileAnalyticsEvent.Logout, EventSupplier.All)
        userClient.logout()
        navigator.navigateAndClearBackStack(Destination.Login)
    }
}
