package app.oguzhanozgokce.midmoney.feature.detail

data class DetailUiState(
    val symbol: String = "",
    val isLoading: Boolean = false,
)

sealed interface DetailUiAction {
    data class Load(val symbol: String) : DetailUiAction
    data object BackClicked : DetailUiAction
}

sealed interface DetailUiEffect
