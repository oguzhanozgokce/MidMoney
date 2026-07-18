package app.oguzhanozgokce.midmoney.feature.market.presentation.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import app.oguzhanozgokce.midmoney.feature.market.R

enum class MarketBannerAction {
    OpenMarketList,
    OpenWatchlist,
}

data class MarketBannerUi(
    val icon: ImageVector,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    @StringRes val actionTextRes: Int,
    val action: MarketBannerAction,
) {
    companion object {
        val defaults: List<MarketBannerUi> = listOf(
            MarketBannerUi(
                icon = Icons.AutoMirrored.Outlined.ShowChart,
                titleRes = R.string.banner_markets_title,
                subtitleRes = R.string.banner_markets_subtitle,
                actionTextRes = R.string.banner_markets_action,
                action = MarketBannerAction.OpenMarketList,
            ),
            MarketBannerUi(
                icon = Icons.Outlined.Star,
                titleRes = R.string.banner_watchlist_title,
                subtitleRes = R.string.banner_watchlist_subtitle,
                actionTextRes = R.string.banner_watchlist_action,
                action = MarketBannerAction.OpenWatchlist,
            ),
            MarketBannerUi(
                icon = Icons.Outlined.Notifications,
                titleRes = R.string.banner_updates_title,
                subtitleRes = R.string.banner_updates_subtitle,
                actionTextRes = R.string.banner_updates_action,
                action = MarketBannerAction.OpenMarketList,
            ),
        )
    }
}
