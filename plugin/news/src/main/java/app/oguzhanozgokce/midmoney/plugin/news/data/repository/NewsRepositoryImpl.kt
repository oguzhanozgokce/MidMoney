package app.oguzhanozgokce.midmoney.plugin.news.data.repository

import app.oguzhanozgokce.midmoney.error.ErrorHandler
import app.oguzhanozgokce.midmoney.plugin.news.data.remote.NewsApi
import app.oguzhanozgokce.midmoney.plugin.news.data.remote.toNewsArticles
import app.oguzhanozgokce.midmoney.plugin.news.domain.model.NewsArticle
import app.oguzhanozgokce.midmoney.plugin.news.domain.repository.NewsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val NEWS_WINDOW_DAYS = 7L
private const val GENERAL_CATEGORY = "general"

internal class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val errorHandler: ErrorHandler,
) : NewsRepository {

    override suspend fun getCompanyNews(symbol: String): Result<List<NewsArticle>> =
        errorHandler.call {
            val to = LocalDate.now()
            val from = to.minusDays(NEWS_WINDOW_DAYS)
            api.companyNews(
                symbol = symbol,
                from = from.format(DateTimeFormatter.ISO_LOCAL_DATE),
                to = to.format(DateTimeFormatter.ISO_LOCAL_DATE),
            ).toNewsArticles()
        }

    override suspend fun getMarketNews(): Result<List<NewsArticle>> =
        errorHandler.call { api.marketNews(GENERAL_CATEGORY).toNewsArticles() }
}
