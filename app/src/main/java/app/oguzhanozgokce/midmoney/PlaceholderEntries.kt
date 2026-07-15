package app.oguzhanozgokce.midmoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.Navigator

/**
 * TEMPORARY navigation scaffolding for Phase 4. Renders placeholder screens so the Navigation 3
 * wiring (back stack, [Navigator] commands, entryProvider multibinding) can be exercised end-to-end.
 * Each of these entries will be replaced by its real `:feature` module in Phase 6.
 */
fun EntryProviderScope<NavKey>.placeholderEntries(navigator: Navigator) {
    entry<Destination.Login> {
        PlaceholderScreen(title = "Login") {
            Button(onClick = { navigator.navigateAndClearBackStack(Destination.Market) }) {
                Text("Enter")
            }
        }
    }
    entry<Destination.Market> {
        PlaceholderScreen(title = "Market") {
            Button(onClick = { navigator.navigate(Destination.Detail(symbol = "AAPL")) }) {
                Text("Open AAPL detail")
            }
            Button(onClick = { navigator.navigate(Destination.Watchlist) }) {
                Text("Open watchlist")
            }
        }
    }
    entry<Destination.Detail> { key ->
        PlaceholderScreen(title = "Detail: ${key.symbol}") {
            Button(onClick = { navigator.goBack() }) { Text("Back") }
        }
    }
    entry<Destination.Watchlist> {
        PlaceholderScreen(title = "Watchlist") {
            Button(onClick = { navigator.goBack() }) { Text("Back") }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String, actions: @Composable () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            actions()
        }
    }
}
