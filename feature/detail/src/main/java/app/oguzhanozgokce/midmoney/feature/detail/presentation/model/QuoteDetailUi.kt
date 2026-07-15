package app.oguzhanozgokce.midmoney.feature.detail.presentation.model

import app.oguzhanozgokce.midmoney.common.extensions.formatPrice
import app.oguzhanozgokce.midmoney.common.extensions.formatSignedPercent
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

data class QuoteDetailUi(
    val current: String,
    val change: String,
    val changePercent: String,
    val open: String,
    val high: String,
    val low: String,
    val previousClose: String,
)

fun Quote.toDetailUi(): QuoteDetailUi = QuoteDetailUi(
    current = current.formatPrice(),
    change = change.formatPrice(),
    changePercent = percentChange.formatSignedPercent(),
    open = open.formatPrice(),
    high = high.formatPrice(),
    low = low.formatPrice(),
    previousClose = previousClose.formatPrice(),
)
