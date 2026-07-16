package app.oguzhanozgokce.midmoney.feature.market.presentation.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi

class MarketUiStatePreviewProvider : PreviewParameterProvider<MarketUiState> {
    override val values = sequenceOf(
        MarketUiState(isLoading = true),
        MarketUiState(
            quotes = listOf(
                QuoteUi(
                    symbol = "AAPL",
                    name = "Apple Inc.",
                    priceText = "150.25",
                    changePercentText = "+1.20%",
                    isPositive = true,
                ),
                QuoteUi(
                    symbol = "TSLA",
                    name = "Tesla Inc.",
                    priceText = "240.10",
                    changePercentText = "-0.85%",
                    isPositive = false,
                ),
            ),
        ),
        MarketUiState(errorMessage = UiText.Dynamic("Something went wrong")),
    )
}
