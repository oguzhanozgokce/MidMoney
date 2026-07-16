package app.oguzhanozgokce.midmoney.feature.detail.presentation.model

import app.oguzhanozgokce.midmoney.plugin.news.domain.model.NewsArticle
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class NewsUi(
    val headline: String,
    val source: String,
    val dateText: String,
    val imageUrl: String,
    val url: String,
)

private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")

fun NewsArticle.toUi(): NewsUi = NewsUi(
    headline = headline,
    source = source,
    dateText = if (publishedEpochSeconds > 0) {
        Instant.ofEpochSecond(publishedEpochSeconds).atZone(ZoneId.systemDefault()).format(dateFormatter)
    } else {
        ""
    },
    imageUrl = imageUrl,
    url = url,
)
