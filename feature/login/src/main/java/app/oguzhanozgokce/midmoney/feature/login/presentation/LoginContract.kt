package app.oguzhanozgokce.midmoney.feature.login.presentation

import app.oguzhanozgokce.midmoney.designsystem.text.UiText

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)

sealed interface LoginUiAction {
    data class EmailChanged(val email: String) : LoginUiAction
    data class PasswordChanged(val password: String) : LoginUiAction
    data object LoginClicked : LoginUiAction
}

sealed interface LoginUiEffect {
    data class ShowMessage(val text: UiText) : LoginUiEffect
}
