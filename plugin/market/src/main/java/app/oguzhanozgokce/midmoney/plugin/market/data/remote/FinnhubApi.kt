package app.oguzhanozgokce.midmoney.plugin.market.data.remote

import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.QuoteDto
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.SymbolSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

internal interface FinnhubApi {

    @GET("quote")
    suspend fun getQuote(@Query("symbol") symbol: String): QuoteDto

    @GET("search")
    suspend fun search(@Query("q") query: String): SymbolSearchResponseDto
}
