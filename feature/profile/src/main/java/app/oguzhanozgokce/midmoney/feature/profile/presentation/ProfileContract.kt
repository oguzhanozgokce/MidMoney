package app.oguzhanozgokce.midmoney.feature.profile.presentation

import app.oguzhanozgokce.midmoney.designsystem.text.UiText

data class ProfileUiState(
    val email: String? = null,
    val versionName: String = "",
    val environmentLabel: String? = null,
)

sealed interface ProfileUiAction {
    data object Logout : ProfileUiAction
    data class ComingSoonClicked(val item: String) : ProfileUiAction
}

sealed interface ProfileUiEffect {
    data class ShowMessage(val message: UiText) : ProfileUiEffect
}
