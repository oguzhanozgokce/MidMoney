package app.oguzhanozgokce.midmoney.feature.detail.presentation

import app.oguzhanozgokce.midmoney.feature.detail.presentation.model.QuoteDetailUi

data class DetailUiState(
    val symbol: String = "",
    val name: String = "",
    val quote: QuoteDetailUi? = null,
    val livePriceText: String? = null,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface DetailUiAction {
    data class Load(val symbol: String) : DetailUiAction
    data object BackClicked : DetailUiAction
    data object Retry : DetailUiAction
    data object BuyClicked : DetailUiAction
    data object SellClicked : DetailUiAction
    data object ToggleSave : DetailUiAction
}

sealed interface DetailUiEffect {
    data class ShowMessage(val message: String) : DetailUiEffect
}
