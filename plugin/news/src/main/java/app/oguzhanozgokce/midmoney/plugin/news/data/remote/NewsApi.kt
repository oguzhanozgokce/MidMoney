package app.oguzhanozgokce.midmoney.plugin.news.data.remote

import app.oguzhanozgokce.midmoney.plugin.news.data.remote.dto.NewsItemDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("company-news")
    suspend fun companyNews(
        @Query("symbol") symbol: String,
        @Query("from") from: String,
        @Query("to") to: String,
    ): List<NewsItemDto>

    @GET("news")
    suspend fun marketNews(@Query("category") category: String): List<NewsItemDto>
}
