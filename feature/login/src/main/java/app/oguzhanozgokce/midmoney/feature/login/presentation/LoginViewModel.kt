package app.oguzhanozgokce.midmoney.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.feature.login.R
import app.oguzhanozgokce.midmoney.feature.login.analytics.LoginAnalyticsEvent
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.user.UserClient
import app.oguzhanozgokce.midmoney.plugin.user.domain.model.AuthError
import app.oguzhanozgokce.midmoney.plugin.user.domain.model.AuthException
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
                emitUiEffect(LoginUiEffect.ShowMessage(UiText.Resource(R.string.login_empty_fields)))
            }
            return
        }
        updateUiState { copy(isLoading = true) }
        analytics.track(LoginAnalyticsEvent.LoginClicked)
        viewModelScope.launch {
            userClient.loginOrRegister(state.email, state.password)
                .onSuccess {
                    analytics.track(LoginAnalyticsEvent.LoginSucceeded)
                    navigator.navigateAndClearBackStack(Destination.Home)
                }
                .onFailure { throwable ->
                    updateUiState { copy(isLoading = false) }
                    val error = (throwable as? AuthException)?.error ?: AuthError.Unknown
                    analytics.track(LoginAnalyticsEvent.LoginFailed(error.name))
                    emitUiEffect(LoginUiEffect.ShowMessage(UiText.Resource(error.toMessageRes())))
                }
        }
    }
}

private fun AuthError.toMessageRes(): Int = when (this) {
    AuthError.WeakPassword -> R.string.login_error_weak_password
    AuthError.InvalidCredentials -> R.string.login_error_invalid_credentials
    AuthError.NoAccount -> R.string.login_error_no_account
    AuthError.Network -> R.string.login_error_network
    AuthError.Unknown -> R.string.login_error_generic
}
