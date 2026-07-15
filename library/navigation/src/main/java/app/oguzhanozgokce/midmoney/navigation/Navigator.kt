package app.oguzhanozgokce.midmoney.navigation

import kotlinx.coroutines.flow.Flow

interface Navigator {
    val commands: Flow<NavigationCommand>

    /** Push a destination onto the back stack. */
    fun navigate(destination: Destination)

    /** Replace the whole back stack with a single destination (e.g. after login/logout). */
    fun navigateAndClearBackStack(destination: Destination)

    /** Pop the top destination. */
    fun goBack()
}

sealed interface NavigationCommand {
    data class Navigate(val destination: Destination) : NavigationCommand
    data class NavigateAndClearBackStack(val destination: Destination) : NavigationCommand
    data object Back : NavigationCommand
}
