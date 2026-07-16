package app.oguzhanozgokce.midmoney

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import app.oguzhanozgokce.midmoney.bottomnav.MidMoneyBottomNav
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.EntryProviderInstaller
import app.oguzhanozgokce.midmoney.navigation.NavigationCommand
import app.oguzhanozgokce.midmoney.navigation.Navigator

@Composable
fun MidMoneyNavDisplay(
    startDestination: NavKey,
    navigator: Navigator,
    entryInstallers: Set<EntryProviderInstaller>,
) {
    val backStack = rememberNavBackStack(startDestination)

    LaunchedEffect(navigator) {
        navigator.commands.collect { command ->
            when (command) {
                is NavigationCommand.Navigate -> backStack.add(command.destination)
                is NavigationCommand.NavigateAndClearBackStack -> {
                    backStack.clear()
                    backStack.add(command.destination)
                }
                NavigationCommand.Back -> backStack.removeLastOrNull()
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Destination.Home> { MidMoneyBottomNav() }
            entryInstallers.forEach { installer -> installer() }
        },
    )
}
