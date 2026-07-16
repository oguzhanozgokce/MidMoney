package app.oguzhanozgokce.midmoney.plugin.news.data.remote

import app.oguzhanozgokce.midmoney.plugin.news.data.remote.dto.NewsItemDto
import app.oguzhanozgokce.midmoney.plugin.news.domain.model.NewsArticle

private const val MAX_NEWS = 20

fun List<NewsItemDto>.toNewsArticles(): List<NewsArticle> =
    asSequence()
        .filter { it.headline.isNotBlank() && it.url.isNotBlank() }
        .map {
            NewsArticle(
                headline = it.headline,
                source = it.source,
                url = it.url,
                imageUrl = it.image,
                publishedEpochSeconds = it.datetime,
            )
        }
        .take(MAX_NEWS)
        .toList()
