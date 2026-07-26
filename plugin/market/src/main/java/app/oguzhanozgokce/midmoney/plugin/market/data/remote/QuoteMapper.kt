package app.oguzhanozgokce.midmoney.plugin.market.data.remote

import app.oguzhanozgokce.midmoney.common.extensions.orZero
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.QuoteDto
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

internal fun QuoteDto.toDomain(symbol: String): Quote = Quote(
    symbol = symbol,
    current = current.orZero(),
    change = change.orZero(),
    percentChange = percentChange.orZero(),
    high = high.orZero(),
    low = low.orZero(),
    open = open.orZero(),
    previousClose = previousClose.orZero(),
)
