package app.oguzhanozgokce.midmoney.feature.detail

import androidx.lifecycle.ViewModel
import app.oguzhanozgokce.midmoney.mvi.MVI
import app.oguzhanozgokce.midmoney.mvi.mvi
import app.oguzhanozgokce.midmoney.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel(),
    MVI<DetailUiState, DetailUiAction, DetailUiEffect> by mvi(DetailUiState()) {

    override fun onAction(uiAction: DetailUiAction) {
        when (uiAction) {
            is DetailUiAction.Load -> updateUiState { copy(symbol = uiAction.symbol) }
            DetailUiAction.BackClicked -> navigator.goBack()
        }
    }
}
