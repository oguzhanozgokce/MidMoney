package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi

class MarketUiStatePreviewProvider : PreviewParameterProvider<MarketUiState> {
    override val values = sequenceOf(
        MarketUiState(isLoading = true),
        MarketUiState(
            quotes = listOf(
                QuoteUi(symbol = "AAPL", priceText = "150.25", changePercentText = "+1.20%", isPositive = true),
                QuoteUi(symbol = "TSLA", priceText = "240.10", changePercentText = "-0.85%", isPositive = false),
            ),
        ),
        MarketUiState(errorMessage = "Something went wrong"),
    )
}
