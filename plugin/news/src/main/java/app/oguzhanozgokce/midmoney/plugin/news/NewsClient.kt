package app.oguzhanozgokce.midmoney.plugin.news

import app.oguzhanozgokce.midmoney.plugin.news.domain.model.NewsArticle
import app.oguzhanozgokce.midmoney.plugin.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsClient @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend fun getCompanyNews(symbol: String): Result<List<NewsArticle>> =
        repository.getCompanyNews(symbol)

    suspend fun getMarketNews(): Result<List<NewsArticle>> = repository.getMarketNews()
}
