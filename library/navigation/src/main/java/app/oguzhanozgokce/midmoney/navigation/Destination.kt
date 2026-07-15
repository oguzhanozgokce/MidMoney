package app.oguzhanozgokce.midmoney.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * All navigation destinations (Nav3 keys). Keys are `@Serializable` so the back stack survives
 * configuration changes and process death. Kept in a shared module so any feature can navigate to
 * any destination without depending on another feature's implementation.
 */
sealed interface Destination : NavKey {

    @Serializable
    data object Login : Destination

    @Serializable
    data object Market : Destination

    @Serializable
    data class Detail(val symbol: String) : Destination

    @Serializable
    data object Watchlist : Destination
}
