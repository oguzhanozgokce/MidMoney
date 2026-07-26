package app.oguzhanozgokce.midmoney.plugin.market.data.remote

import app.oguzhanozgokce.midmoney.network.di.NetworkModule
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.QuoteDto
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.SymbolSearchResponseDto
import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.decodeFromString
import org.junit.Test

class MarketDtoJsonTest {

    private val json = NetworkModule.provideJson()

    @Test
    fun `omitted quote fields decode to null rather than zero`() {
        val dto = json.decodeFromString<QuoteDto>("{}")

        assertThat(dto.current).isNull()
        assertThat(dto.percentChange).isNull()
    }

    @Test
    fun `explicitly null quote fields decode to null`() {
        val dto = json.decodeFromString<QuoteDto>("""{"c":null,"dp":null}""")

        assertThat(dto.current).isNull()
        assertThat(dto.percentChange).isNull()
    }

    @Test
    fun `a real quote payload maps onto the domain model`() {
        val dto = json.decodeFromString<QuoteDto>(
            """{"c":150.25,"d":1.2,"dp":0.8,"h":151.0,"l":149.0,"o":150.0,"pc":149.05}""",
        )

        val quote = dto.toDomain("AAPL")

        assertThat(quote.symbol).isEqualTo("AAPL")
        assertThat(quote.current).isEqualTo(150.25)
        assertThat(quote.percentChange).isEqualTo(0.8)
    }

    @Test
    fun `a missing price becomes zero only in the domain layer, not in the DTO`() {
        val dto = json.decodeFromString<QuoteDto>("{}")

        assertThat(dto.current).isNull()
        assertThat(dto.toDomain("AAPL").current).isEqualTo(0.0)
    }

    @Test
    fun `unknown keys are ignored instead of failing the response`() {
        val dto = json.decodeFromString<QuoteDto>("""{"c":10.0,"somethingNew":"surprise"}""")

        assertThat(dto.current).isEqualTo(10.0)
    }

    @Test
    fun `omitted search fields fall back to their defaults`() {
        val dto = json.decodeFromString<SymbolSearchResponseDto>("{}")

        assertThat(dto.count).isEqualTo(0)
        assertThat(dto.result).isEmpty()
    }

    @Test
    fun `explicit nulls are coerced to defaults instead of failing the response`() {
        val dto = json.decodeFromString<SymbolSearchResponseDto>("""{"count":null,"result":null}""")

        assertThat(dto.count).isEqualTo(0)
        assertThat(dto.result).isEmpty()
    }

    @Test
    fun `an explicit null inside a result entry is coerced, keeping the rest of the list`() {
        val dto = json.decodeFromString<SymbolSearchResponseDto>(
            """{"count":2,"result":[{"symbol":null,"description":"NO TICKER"},
               {"symbol":"AAPL","description":"APPLE INC"}]}""",
        )

        assertThat(dto.result.map { it.symbol }).containsExactly("", "AAPL").inOrder()
        assertThat(dto.toDomain().map { it.symbol }).containsExactly("AAPL")
    }

    @Test
    fun `search results drop exchange-suffixed and unnamed tickers`() {
        val dto = json.decodeFromString<SymbolSearchResponseDto>(
            """{"count":3,"result":[{"symbol":"AAPL","description":"APPLE INC"},
               {"symbol":"AAPL.MX","description":"APPLE INC"},
               {"symbol":"NONAME","description":""}]}""",
        )

        assertThat(dto.toDomain().map { it.symbol }).containsExactly("AAPL")
    }
}
