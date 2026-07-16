package app.oguzhanozgokce.midmoney.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Destination : NavKey {

    @Serializable
    data object Login : Destination

    @Serializable
    data object Home : Destination

    @Serializable
    data object Markets : Destination

    @Serializable
    data class Detail(val symbol: String) : Destination
}
