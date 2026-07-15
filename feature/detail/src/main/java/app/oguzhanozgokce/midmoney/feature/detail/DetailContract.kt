package app.oguzhanozgokce.midmoney.feature.detail

data class DetailUiState(
    val symbol: String = "",
    val quote: QuoteDetailUi? = null,
    val livePriceText: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface DetailUiAction {
    data class Load(val symbol: String) : DetailUiAction
    data object BackClicked : DetailUiAction
}

sealed interface DetailUiEffect
