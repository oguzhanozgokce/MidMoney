package app.oguzhanozgokce.midmoney.feature.detail.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.feature.detail.presentation.model.QuoteDetailUi

class DetailUiStatePreviewProvider : PreviewParameterProvider<DetailUiState> {
    override val values = sequenceOf(
        DetailUiState(symbol = "AAPL", isLoading = true),
        DetailUiState(
            symbol = "AAPL",
            livePriceText = "150.42",
            quote = QuoteDetailUi(
                current = "150.25",
                change = "+1.20",
                changePercent = "+0.80%",
                open = "149.00",
                high = "151.30",
                low = "148.80",
                previousClose = "149.05",
            ),
        ),
        DetailUiState(symbol = "AAPL", errorMessage = "Something went wrong"),
    )
}
