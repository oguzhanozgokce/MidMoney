package app.oguzhanozgokce.midmoney.feature.marketlist.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.feature.marketlist.presentation.model.QuoteUi

class MarketListUiStatePreviewProvider : PreviewParameterProvider<MarketListUiState> {
    override val values = sequenceOf(
        MarketListUiState(isLoading = true),
        MarketListUiState(
            quotes = listOf(
                QuoteUi("AAPL", "Apple Inc.", "150.25", "+1.20%", isPositive = true),
                QuoteUi("TSLA", "Tesla Inc.", "240.10", "-0.85%", isPositive = false),
            ),
        ),
    )
}
