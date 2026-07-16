package app.oguzhanozgokce.midmoney.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.oguzhanozgokce.midmoney.feature.market.presentation.home.MarketRoute
import app.oguzhanozgokce.midmoney.feature.profile.presentation.ProfileRoute
import app.oguzhanozgokce.midmoney.feature.watchlist.presentation.WatchlistRoute

private enum class HomeTab(val label: String, val icon: ImageVector) {
    Market("Home", Icons.AutoMirrored.Outlined.ShowChart),
    Watchlist("Watchlist", Icons.Outlined.Star),
    Profile("Profile", Icons.Outlined.Person),
}

/**
 * The signed-in shell. Hosts the bottom navigation and swaps between the top-level tab routes
 * locally, so tab switching never touches the global back stack. Detail is still pushed above this
 * shell by the market tab.
 *
 * Owns edge-to-edge insets for its tabs: the Scaffold applies the status-bar inset on top and the
 * navigation-bar inset to the bottom bar, then [consumeWindowInsets] stops the tab screens' own
 * scaffolds from adding the same insets a second time.
 */
@Composable
fun HomeShell() {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.Market) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp,
            ) {
                HomeTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                        label = { Text(text = tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            when (selectedTab) {
                HomeTab.Market -> MarketRoute(onOpenWatchlist = { selectedTab = HomeTab.Watchlist })
                HomeTab.Watchlist -> WatchlistRoute()
                HomeTab.Profile -> ProfileRoute()
            }
        }
    }
}
