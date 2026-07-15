package app.oguzhanozgokce.midmoney.feature.profile.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ProfileUiStatePreviewProvider : PreviewParameterProvider<ProfileUiState> {
    override val values = sequenceOf(
        ProfileUiState(email = "user@midmoney.app"),
        ProfileUiState(email = null),
    )
}
