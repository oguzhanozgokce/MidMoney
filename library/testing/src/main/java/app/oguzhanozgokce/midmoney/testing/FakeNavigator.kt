package app.oguzhanozgokce.midmoney.testing

import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.NavigationCommand
import app.oguzhanozgokce.midmoney.navigation.Navigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeNavigator : Navigator {
    val commandsLog: MutableList<NavigationCommand> = mutableListOf()

    override val commands: Flow<NavigationCommand> = emptyFlow()

    override fun navigate(destination: Destination) {
        commandsLog += NavigationCommand.Navigate(destination)
    }

    override fun navigateAndClearBackStack(destination: Destination) {
        commandsLog += NavigationCommand.NavigateAndClearBackStack(destination)
    }

    override fun goBack() {
        commandsLog += NavigationCommand.Back
    }
}
