package app.oguzhanozgokce.midmoney.feature.detail.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.feature.detail.presentation.model.NewsUi
import app.oguzhanozgokce.midmoney.feature.detail.presentation.model.QuoteDetailUi

class DetailUiStatePreviewProvider : PreviewParameterProvider<DetailUiState> {
    override val values = sequenceOf(
        DetailUiState(symbol = "AAPL", name = "Apple Inc.", isLoading = true),
        DetailUiState(
            symbol = "AAPL",
            name = "Apple Inc.",
            livePriceText = "150.42",
            quote = QuoteDetailUi(
                current = "150.25",
                change = "+1.20",
                changePercent = "+0.80%",
                isPositive = true,
                open = "149.00",
                high = "151.30",
                low = "148.80",
                previousClose = "149.05",
            ),
            news = listOf(
                NewsUi(
                    headline = "Apple unveils new lineup at fall event",
                    source = "Reuters",
                    dateText = "12 Jul",
                    imageUrl = "",
                    url = "https://example.com/a",
                ),
                NewsUi(
                    headline = "Analysts raise price targets after strong quarter",
                    source = "Bloomberg",
                    dateText = "11 Jul",
                    imageUrl = "",
                    url = "https://example.com/b",
                ),
            ),
        ),
        DetailUiState(
            symbol = "AAPL",
            name = "Apple Inc.",
            errorMessage = UiText.Dynamic("HTTP 429 Too Many Requests"),
        ),
    )
}
