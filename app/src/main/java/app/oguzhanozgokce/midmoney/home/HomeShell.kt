package app.oguzhanozgokce.midmoney.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import app.oguzhanozgokce.midmoney.feature.market.presentation.MarketRoute
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
 */
@Composable
fun HomeShell() {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.Market) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                HomeTab.Market -> MarketRoute(onOpenWatchlist = { selectedTab = HomeTab.Watchlist })
                HomeTab.Watchlist -> WatchlistRoute()
                HomeTab.Profile -> ProfileRoute()
            }
        }
        NavigationBar {
            HomeTab.entries.forEach { tab ->
                NavigationBarItem(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                    label = { Text(text = tab.label) },
                )
            }
        }
    }
}
