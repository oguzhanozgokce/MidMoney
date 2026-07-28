package app.oguzhanozgokce.midmoney.feature.watchlist.presentation.model

import app.oguzhanozgokce.midmoney.common.extensions.formatPrice
import app.oguzhanozgokce.midmoney.common.extensions.formatSignedPercent
import app.oguzhanozgokce.midmoney.plugin.market.domain.CompanyNames
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

/**
 * A market row as this screen renders it: the numbers are already formatted and [isPositive] exists
 * only to pick a colour.
 *
 * Each feature owns its copy on purpose. The three screens happen to show the same shape today, but
 * a shared model would become a superset the moment one of them needs an extra column — and keeping
 * it here means no plugin type crosses into composition, so the compiler can infer stability without
 * a stability-configuration file.
 */
data class QuoteUi(
    val symbol: String,
    val name: String,
    val priceText: String,
    val changePercentText: String,
    val isPositive: Boolean,
)

fun Quote.toUi(): QuoteUi = QuoteUi(
    symbol = symbol,
    name = CompanyNames.of(symbol),
    priceText = current.formatPrice(),
    changePercentText = percentChange.formatSignedPercent(),
    isPositive = percentChange >= 0,
)
