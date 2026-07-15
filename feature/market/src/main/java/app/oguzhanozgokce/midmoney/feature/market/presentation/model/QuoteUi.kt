package app.oguzhanozgokce.midmoney.feature.market.presentation.model

import app.oguzhanozgokce.midmoney.common.extensions.formatPrice
import app.oguzhanozgokce.midmoney.common.extensions.formatSignedPercent
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

data class QuoteUi(
    val symbol: String,
    val priceText: String,
    val changePercentText: String,
    val isPositive: Boolean,
)

fun Quote.toUi(): QuoteUi = QuoteUi(
    symbol = symbol,
    priceText = current.formatPrice(),
    changePercentText = percentChange.formatSignedPercent(),
    isPositive = percentChange >= 0,
)
