package app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto

import kotlinx.serialization.Serializable

/** Finnhub `/search` response, e.g. `{"count":4,"result":[{"symbol":"AAPL","description":"APPLE INC",...}]}`. */
@Serializable
data class SymbolSearchResponseDto(
    val count: Int? = null,
    val result: List<SymbolMatchDto>? = null,
)

@Serializable
data class SymbolMatchDto(
    val symbol: String? = null,
    val description: String? = null,
    val displaySymbol: String? = null,
    val type: String? = null,
)
