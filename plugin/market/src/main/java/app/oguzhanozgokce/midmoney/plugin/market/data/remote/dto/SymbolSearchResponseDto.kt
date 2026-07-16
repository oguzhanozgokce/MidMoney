package app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto

import kotlinx.serialization.Serializable

/** Finnhub `/search` response, e.g. `{"count":4,"result":[{"symbol":"AAPL","description":"APPLE INC",...}]}`. */
@Serializable
data class SymbolSearchResponseDto(
    val count: Int = 0,
    val result: List<SymbolMatchDto> = emptyList(),
)

@Serializable
data class SymbolMatchDto(
    val symbol: String = "",
    val description: String = "",
    val displaySymbol: String = "",
    val type: String = "",
)
