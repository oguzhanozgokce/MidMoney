package app.oguzhanozgokce.midmoney.plugin.news.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class NewsItemDto(
    val headline: String? = null,
    val summary: String? = null,
    val source: String? = null,
    val url: String? = null,
    val image: String? = null,
    val datetime: Long? = null,
)
