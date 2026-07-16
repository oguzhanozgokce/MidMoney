package app.oguzhanozgokce.midmoney.feature.marketlist.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.feature.marketlist.presentation.model.QuoteUi
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch

class MarketListUiStatePreviewProvider : PreviewParameterProvider<MarketListUiState> {
    override val values = sequenceOf(
        // Loading the curated list.
        MarketListUiState(isLoading = true),
        // Loaded list.
        MarketListUiState(
            quotes = listOf(
                QuoteUi("AAPL", "Apple Inc.", "150.25", "+1.20%", isPositive = true),
                QuoteUi("TSLA", "Tesla Inc.", "240.10", "-0.85%", isPositive = false),
            ),
        ),
        // Failed to load the list.
        MarketListUiState(errorMessage = "HTTP 429 Too Many Requests"),
        // Search with results.
        MarketListUiState(
            query = "app",
            results = listOf(
                SymbolMatch(symbol = "AAPL", description = "Apple Inc."),
                SymbolMatch(symbol = "APP", description = "AppLovin Corp."),
            ),
        ),
        // Search with no matches.
        MarketListUiState(query = "zzzz", results = emptyList()),
    )
}
