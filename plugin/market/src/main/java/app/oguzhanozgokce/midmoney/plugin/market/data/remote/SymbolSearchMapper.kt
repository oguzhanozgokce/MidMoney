package app.oguzhanozgokce.midmoney.plugin.market.data.remote

import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.SymbolSearchResponseDto
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch

private const val MAX_RESULTS = 25

/**
 * Keeps plain tickers with a name and drops exchange-suffixed variants (e.g. "AAPL.MX"), which the
 * free /quote endpoint does not price. Capped so the list stays manageable.
 */
fun SymbolSearchResponseDto.toDomain(): List<SymbolMatch> =
    result.orEmpty().asSequence()
        .map { SymbolMatch(symbol = it.symbol.orEmpty(), description = it.description.orEmpty()) }
        .filter { it.symbol.isNotBlank() && it.description.isNotBlank() && !it.symbol.contains('.') }
        .distinctBy { it.symbol }
        .take(MAX_RESULTS)
        .toList()
