package app.oguzhanozgokce.midmoney.feature.detail.presentation.model

import app.oguzhanozgokce.midmoney.plugin.news.domain.model.NewsArticle
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class NewsUi(
    val headline: String,
    val metaText: String,
    val imageUrl: String,
    val url: String,
)

private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")

fun NewsArticle.toUi(): NewsUi {
    val dateText = if (publishedEpochSeconds > 0) {
        Instant.ofEpochSecond(publishedEpochSeconds).atZone(ZoneId.systemDefault()).format(dateFormatter)
    } else {
        ""
    }
    return NewsUi(
        headline = headline,
        metaText = listOf(source, dateText).filter { it.isNotBlank() }.joinToString(" · "),
        imageUrl = imageUrl,
        url = url,
    )
}
