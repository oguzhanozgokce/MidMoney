package app.oguzhanozgokce.midmoney.feature.market.presentation.model

import androidx.compose.ui.graphics.Color

/**
 * A promotional banner shown in the home pager. There is no campaign endpoint on the free tier, so
 * these are static presentation content; in a real app they would come from remote config.
 */
data class HomeBannerUi(
    val title: String,
    val subtitle: String,
    val gradient: List<Color>,
) {
    companion object {
        val defaults: List<HomeBannerUi> = listOf(
            HomeBannerUi(
                title = "Track the market live",
                subtitle = "Real-time prices, right in your pocket.",
                gradient = listOf(Color(0xFF4959EA), Color(0xFF7C4DFF)),
            ),
            HomeBannerUi(
                title = "Build your watchlist",
                subtitle = "Follow the symbols that matter to you.",
                gradient = listOf(Color(0xFF00B0FF), Color(0xFF2962FF)),
            ),
            HomeBannerUi(
                title = "Never miss a move",
                subtitle = "Live trade updates on every quote.",
                gradient = listOf(Color(0xFF1DE9B6), Color(0xFF00897B)),
            ),
        )
    }
}
