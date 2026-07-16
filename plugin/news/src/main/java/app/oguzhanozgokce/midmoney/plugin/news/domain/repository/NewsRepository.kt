package app.oguzhanozgokce.midmoney.plugin.news.domain.repository

import app.oguzhanozgokce.midmoney.plugin.news.domain.model.NewsArticle

interface NewsRepository {
    suspend fun getCompanyNews(symbol: String): Result<List<NewsArticle>>
    suspend fun getMarketNews(): Result<List<NewsArticle>>
}
