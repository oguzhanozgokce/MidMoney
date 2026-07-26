package app.oguzhanozgokce.midmoney.plugin.news.data.remote

import app.oguzhanozgokce.midmoney.common.extensions.orZero
import app.oguzhanozgokce.midmoney.plugin.news.data.remote.dto.NewsItemDto
import app.oguzhanozgokce.midmoney.plugin.news.domain.model.NewsArticle

private const val MAX_NEWS = 20

internal fun List<NewsItemDto>.toNewsArticles(): List<NewsArticle> =
    asSequence()
        .map {
            NewsArticle(
                headline = it.headline.orEmpty(),
                source = it.source.orEmpty(),
                url = it.url.orEmpty(),
                imageUrl = it.image.orEmpty(),
                publishedEpochSeconds = it.datetime.orZero(),
            )
        }
        .filter { it.headline.isNotBlank() && it.url.isNotBlank() }
        .take(MAX_NEWS)
        .toList()
