package app.oguzhanozgokce.midmoney.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigator: Navigator,
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
        // Real authentication will arrive with :plugin:user; for now go straight to the market.
        navigator.navigateAndClearBackStack(Destination.Market)
    }
}
