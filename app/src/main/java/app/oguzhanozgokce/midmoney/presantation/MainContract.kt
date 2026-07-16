package app.oguzhanozgokce.midmoney.presantation

import androidx.navigation3.runtime.NavKey
import app.oguzhanozgokce.midmoney.navigation.Destination

data class MainUiState(
    val startDestination: NavKey = Destination.Login,
)

sealed interface MainUiAction

sealed interface MainUiEffect
