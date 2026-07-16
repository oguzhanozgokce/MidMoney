package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.QuoteUi

class MarketsUiStatePreviewProvider : PreviewParameterProvider<MarketsUiState> {
    override val values = sequenceOf(
        MarketsUiState(isLoading = true),
        MarketsUiState(
            quotes = listOf(
                QuoteUi("AAPL", "Apple Inc.", "150.25", "+1.20%", isPositive = true),
                QuoteUi("TSLA", "Tesla Inc.", "240.10", "-0.85%", isPositive = false),
            ),
        ),
    )
}
