package app.oguzhanozgokce.midmoney.plugin.news.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewsItemDto(
    val headline: String = "",
    val summary: String = "",
    val source: String = "",
    val url: String = "",
    val image: String = "",
    val datetime: Long = 0,
)
