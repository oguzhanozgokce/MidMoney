package app.oguzhanozgokce.midmoney.navigation

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.oguzhanozgokce.midmoney.R
import app.oguzhanozgokce.midmoney.feature.market.presentation.MarketRoute
import app.oguzhanozgokce.midmoney.feature.profile.presentation.ProfileRoute
import app.oguzhanozgokce.midmoney.feature.watchlist.presentation.WatchlistRoute

private enum class MainTab(@StringRes val labelRes: Int, val icon: ImageVector) {
    Market(R.string.nav_market, Icons.AutoMirrored.Outlined.ShowChart),
    Watchlist(R.string.nav_watchlist, Icons.Outlined.Star),
    Profile(R.string.nav_profile, Icons.Outlined.Person),
}

@Composable
fun MidMoneyBottomNav() {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Market) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp,
            ) {
                MainTab.entries.forEach { tab ->
                    val label = stringResource(tab.labelRes)
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(imageVector = tab.icon, contentDescription = label) },
                        label = { Text(text = label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
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
                MainTab.Market -> MarketRoute(onOpenWatchlist = { selectedTab = MainTab.Watchlist })
                MainTab.Watchlist -> WatchlistRoute()
                MainTab.Profile -> ProfileRoute()
            }
        }
    }
}
