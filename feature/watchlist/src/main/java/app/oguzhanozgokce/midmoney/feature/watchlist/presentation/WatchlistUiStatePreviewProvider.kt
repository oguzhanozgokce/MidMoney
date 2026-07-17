package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.plugin.market.ui.QuoteUi

class WatchlistUiStatePreviewProvider : PreviewParameterProvider<WatchlistUiState> {
    override val values = sequenceOf(
        WatchlistUiState(
            items = listOf(
                QuoteUi("AAPL", "Apple Inc.", "150.25", "+1.20%", isPositive = true),
                QuoteUi("TSLA", "Tesla Inc.", "242.80", "-2.35%", isPositive = false),
            ),
        ),
        WatchlistUiState(isLoading = true),
        WatchlistUiState(),
        WatchlistUiState(errorMessage = UiText.Dynamic("Couldn't load your watchlist.")),
    )
}
