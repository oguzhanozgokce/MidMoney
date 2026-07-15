package app.oguzhanozgokce.midmoney.feature.login.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class LoginUiStatePreviewProvider : PreviewParameterProvider<LoginUiState> {
    override val values = sequenceOf(
        LoginUiState(),
        LoginUiState(email = "user@midmoney.app", password = "secret"),
        LoginUiState(email = "user@midmoney.app", password = "secret", isLoading = true),
    )
}
