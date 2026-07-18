package app.oguzhanozgokce.midmoney.navigation

import kotlinx.coroutines.flow.Flow

interface Navigator {
    val commands: Flow<NavigationCommand>

    fun navigate(destination: Destination)
    fun navigateAndClearBackStack(destination: Destination)
    fun goBack()
}

sealed interface NavigationCommand {
    data class Navigate(val destination: Destination) : NavigationCommand
    data class NavigateAndClearBackStack(val destination: Destination) : NavigationCommand
    data object Back : NavigationCommand
}
