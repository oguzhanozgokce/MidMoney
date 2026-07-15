package app.oguzhanozgokce.midmoney.feature.detail

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

data class DetailUiState(
    val symbol: String = "",
    val quote: Quote? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface DetailUiAction {
    data class Load(val symbol: String) : DetailUiAction
    data object BackClicked : DetailUiAction
}

sealed interface DetailUiEffect
