package app.oguzhanozgokce.midmoney.feature.market.presentation.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import app.oguzhanozgokce.midmoney.feature.market.R

data class HomeBannerUi(
    val icon: ImageVector,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    @StringRes val actionTextRes: Int,
) {
    companion object {
        val defaults: List<HomeBannerUi> = listOf(
            HomeBannerUi(
                icon = Icons.AutoMirrored.Outlined.ShowChart,
                titleRes = R.string.banner_markets_title,
                subtitleRes = R.string.banner_markets_subtitle,
                actionTextRes = R.string.banner_markets_action,
            ),
            HomeBannerUi(
                icon = Icons.Outlined.Star,
                titleRes = R.string.banner_watchlist_title,
                subtitleRes = R.string.banner_watchlist_subtitle,
                actionTextRes = R.string.banner_watchlist_action,
            ),
            HomeBannerUi(
                icon = Icons.Outlined.Notifications,
                titleRes = R.string.banner_updates_title,
                subtitleRes = R.string.banner_updates_subtitle,
                actionTextRes = R.string.banner_updates_action,
            ),
        )
    }
}
