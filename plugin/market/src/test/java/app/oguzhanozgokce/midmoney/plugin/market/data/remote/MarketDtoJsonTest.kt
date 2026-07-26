package app.oguzhanozgokce.midmoney.plugin.market.data.remote

import app.oguzhanozgokce.midmoney.network.di.NetworkModule
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.QuoteDto
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.SymbolSearchResponseDto
import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.decodeFromString
import org.junit.Test

/**
 * Pins the DTO convention against the shapes Finnhub actually sends: omitted fields, explicit nulls
 * and unknown keys.
 *
 * The convention is that **every DTO field is nullable** — the wire is untrusted, so "missing" is
 * always possible — and the **mapper** normalizes (`orEmpty()` / `orZero()`) plus drops entries that
 * are unusable. Nullable fields absorb both an omitted key and an explicit `null` without any
 * Json-level value coercion, which is why `provideJson()` needs no `coerceInputValues`.
 *
 * Uses the production [NetworkModule.provideJson] instance on purpose — a copy of the config here
 * would drift from the real one and prove nothing.
 */
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
    fun `unknown keys are ignored instead of failing the response`() {
        val dto = json.decodeFromString<QuoteDto>("""{"c":10.0,"somethingNew":"surprise"}""")

        assertThat(dto.current).isEqualTo(10.0)
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
    fun `a missing price stays null in the DTO and becomes zero only in the domain model`() {
        val dto = json.decodeFromString<QuoteDto>("{}")

        assertThat(dto.current).isNull()
        assertThat(dto.toDomain("AAPL").current).isEqualTo(0.0)
    }

    @Test
    fun `omitted search fields decode to null and map to an empty list`() {
        val dto = json.decodeFromString<SymbolSearchResponseDto>("{}")

        assertThat(dto.result).isNull()
        assertThat(dto.toDomain()).isEmpty()
    }

    @Test
    fun `explicit nulls in the search response do not fail decoding`() {
        val dto = json.decodeFromString<SymbolSearchResponseDto>("""{"count":null,"result":null}""")

        assertThat(dto.count).isNull()
        assertThat(dto.toDomain()).isEmpty()
    }

    @Test
    fun `an entry with a null symbol is dropped, keeping the rest of the list`() {
        val dto = json.decodeFromString<SymbolSearchResponseDto>(
            """{"count":2,"result":[{"symbol":null,"description":"NO TICKER"},
               {"symbol":"AAPL","description":"APPLE INC"}]}""",
        )

        assertThat(dto.result).hasSize(2)
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
