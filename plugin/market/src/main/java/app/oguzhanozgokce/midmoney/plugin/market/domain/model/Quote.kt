package app.oguzhanozgokce.midmoney.plugin.market.domain.model

data class Quote(
    val symbol: String,
    val current: Double,
    val change: Double,
    val percentChange: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val previousClose: Double,
)
