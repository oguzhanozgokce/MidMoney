package app.oguzhanozgokce.midmoney.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.feature.login.analytics.LoginAnalyticsEvent
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.user.UserClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userClient: UserClient,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel(),
    MVI<LoginUiState, LoginUiAction, LoginUiEffect> by mvi(LoginUiState()) {

    override fun onAction(uiAction: LoginUiAction) {
        when (uiAction) {
            is LoginUiAction.EmailChanged -> updateUiState { copy(email = uiAction.email) }
            is LoginUiAction.PasswordChanged -> updateUiState { copy(password = uiAction.password) }
            LoginUiAction.LoginClicked -> login()
        }
    }

    private fun login() {
        val state = currentUiState
        if (state.email.isBlank() || state.password.isBlank()) {
            viewModelScope.launch {
                emitUiEffect(LoginUiEffect.ShowMessage("Enter your email and password"))
            }
            return
        }
        updateUiState { copy(isLoading = true) }
        analytics.track(LoginAnalyticsEvent.LoginClicked)
        viewModelScope.launch {
            userClient.login(state.email, state.password)
                .onSuccess {
                    analytics.track(LoginAnalyticsEvent.LoginSucceeded)
                    navigator.navigateAndClearBackStack(Destination.Market)
                }
                .onFailure { throwable ->
                    updateUiState { copy(isLoading = false) }
                    analytics.track(LoginAnalyticsEvent.LoginFailed(throwable.message ?: "unknown"))
                    emitUiEffect(LoginUiEffect.ShowMessage(throwable.message ?: "Login failed"))
                }
        }
    }
}
