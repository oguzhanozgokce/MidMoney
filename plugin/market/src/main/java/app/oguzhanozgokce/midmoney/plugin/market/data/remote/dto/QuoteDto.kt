package app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Finnhub `/quote` response. Fields are nullable because the API returns zeros/omits fields for
 * unknown symbols or when rate-limited.
 */
@Serializable
data class QuoteDto(
    @SerialName("c") val current: Double? = null,
    @SerialName("d") val change: Double? = null,
    @SerialName("dp") val percentChange: Double? = null,
    @SerialName("h") val high: Double? = null,
    @SerialName("l") val low: Double? = null,
    @SerialName("o") val open: Double? = null,
    @SerialName("pc") val previousClose: Double? = null,
)
