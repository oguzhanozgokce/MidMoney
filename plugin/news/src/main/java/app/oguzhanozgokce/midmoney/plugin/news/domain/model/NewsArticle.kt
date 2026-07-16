package app.oguzhanozgokce.midmoney.plugin.news.domain.model

data class NewsArticle(
    val headline: String,
    val source: String,
    val url: String,
    val imageUrl: String,
    val publishedEpochSeconds: Long,
)
