package app.oguzhanozgokce.midmoney.feature.market.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A promotional banner shown in the home pager. There is no campaign endpoint on the free tier, so
 * these are static presentation content; in a real app they would come from remote config.
 */
data class HomeBannerUi(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val actionText: String,
) {
    companion object {
        val defaults: List<HomeBannerUi> = listOf(
            HomeBannerUi(
                icon = Icons.AutoMirrored.Outlined.ShowChart,
                title = "Track the market live",
                subtitle = "Real-time prices for the symbols you care about, right in your pocket.",
                actionText = "Explore markets",
            ),
            HomeBannerUi(
                icon = Icons.Outlined.Star,
                title = "Build your watchlist",
                subtitle = "Keep an eye on your favorite stocks and follow every move.",
                actionText = "Open watchlist",
            ),
            HomeBannerUi(
                icon = Icons.Outlined.Notifications,
                title = "Never miss a move",
                subtitle = "Live trade updates keep every quote fresh as the market shifts.",
                actionText = "See details",
            ),
        )
    }
}
