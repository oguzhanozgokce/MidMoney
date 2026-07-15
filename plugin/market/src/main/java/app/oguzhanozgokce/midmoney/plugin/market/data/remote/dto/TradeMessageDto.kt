package app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Finnhub WebSocket message, e.g. `{"type":"trade","data":[{"s":"AAPL","p":123.4}]}`. */
@Serializable
data class TradeMessageDto(
    val type: String? = null,
    val data: List<TradeDto>? = null,
)

@Serializable
data class TradeDto(
    @SerialName("s") val symbol: String? = null,
    @SerialName("p") val price: Double? = null,
)
