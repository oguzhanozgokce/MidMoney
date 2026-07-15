package app.oguzhanozgokce.midmoney.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Buffers navigation commands in a [Channel] so a ViewModel can emit them synchronously while the
 * app layer collects them on the main thread and mutates the back stack.
 */
@Singleton
class DefaultNavigator @Inject constructor() : Navigator {

    private val channel = Channel<NavigationCommand>(Channel.BUFFERED)

    override val commands: Flow<NavigationCommand> = channel.receiveAsFlow()

    override fun navigate(destination: Destination) {
        channel.trySend(NavigationCommand.Navigate(destination))
    }

    override fun navigateAndClearBackStack(destination: Destination) {
        channel.trySend(NavigationCommand.NavigateAndClearBackStack(destination))
    }

    override fun goBack() {
        channel.trySend(NavigationCommand.Back)
    }
}
