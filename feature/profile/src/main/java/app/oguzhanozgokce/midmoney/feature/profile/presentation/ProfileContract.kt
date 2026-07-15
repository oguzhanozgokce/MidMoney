package app.oguzhanozgokce.midmoney.feature.profile.presentation

data class ProfileUiState(
    val email: String? = null,
)

sealed interface ProfileUiAction {
    data object Logout : ProfileUiAction
}

sealed interface ProfileUiEffect
