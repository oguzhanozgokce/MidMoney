package app.oguzhanozgokce.midmoney.feature.marketlist.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.feature.marketlist.presentation.model.QuoteUi
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch

class MarketListUiStatePreviewProvider : PreviewParameterProvider<MarketListUiState> {
    override val values = sequenceOf(
        MarketListUiState(isLoading = true),
        MarketListUiState(
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
        MarketListUiState(errorMessage = UiText.Dynamic("HTTP 429 Too Many Requests")),
        MarketListUiState(
            query = "app",
            results = listOf(
                SymbolMatch(symbol = "AAPL", description = "Apple Inc."),
                SymbolMatch(symbol = "APP", description = "AppLovin Corp."),
            ),
        ),
        MarketListUiState(query = "zzzz", results = emptyList()),
    )
}
