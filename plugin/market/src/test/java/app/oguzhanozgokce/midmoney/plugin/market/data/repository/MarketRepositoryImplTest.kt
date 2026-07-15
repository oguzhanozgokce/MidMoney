package app.oguzhanozgokce.midmoney.plugin.market.data.repository

import app.cash.turbine.test
import app.oguzhanozgokce.midmoney.common.coroutines.DispatcherProvider
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.FinnhubApi
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.FinnhubTradeStream
import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.QuoteDto
import app.oguzhanozgokce.midmoney.websocket.WebSocketClient
import app.oguzhanozgokce.midmoney.websocket.WebSocketEvent
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarketRepositoryImplTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val dispatchers = TestDispatcherProvider(UnconfinedTestDispatcher())

    private fun repository(
        api: FinnhubApi = FakeFinnhubApi(),
        webSocketClient: WebSocketClient = FakeWebSocketClient(),
    ) = MarketRepositoryImpl(
        api = api,
        tradeStream = FinnhubTradeStream(webSocketClient, json),
        dispatchers = dispatchers,
    )

    @Test
    fun `getQuote maps the response to a domain quote`() = runTest {
        val api = FakeFinnhubApi(quote = QuoteDto(current = 150.0, percentChange = 1.5))

        val result = repository(api = api).getQuote("AAPL")

        assertThat(result.isSuccess).isTrue()
        val quote = result.getOrThrow()
        assertThat(quote.symbol).isEqualTo("AAPL")
        assertThat(quote.current).isEqualTo(150.0)
        assertThat(quote.percentChange).isEqualTo(1.5)
    }

    @Test
    fun `getQuote returns failure when the api throws`() = runTest {
        val api = FakeFinnhubApi(error = RuntimeException("network error"))

        val result = repository(api = api).getQuote("AAPL")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `getQuotes returns one quote per requested symbol`() = runTest {
        val api = FakeFinnhubApi(quote = QuoteDto(current = 10.0))

        val result = repository(api = api).getQuotes(listOf("AAPL", "MSFT"))

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow().map { it.symbol }).containsExactly("AAPL", "MSFT")
    }

    @Test
    fun `observePrice emits the price parsed from a trade message`() = runTest {
        val events = flowOf(
            WebSocketEvent.MessageReceived("""{"type":"trade","data":[{"s":"AAPL","p":150.0}]}"""),
        )

        repository(webSocketClient = FakeWebSocketClient(events)).observePrice("AAPL").test {
            assertThat(awaitItem()).isEqualTo(150.0)
            awaitComplete()
        }
    }
}

private class TestDispatcherProvider(dispatcher: CoroutineDispatcher) : DispatcherProvider {
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val main: CoroutineDispatcher = dispatcher
}

private class FakeFinnhubApi(
    private val quote: QuoteDto = QuoteDto(),
    private val error: Throwable? = null,
) : FinnhubApi {
    override suspend fun getQuote(symbol: String): QuoteDto {
        error?.let { throw it }
        return quote
    }
}

private class FakeWebSocketClient(
    private val events: Flow<WebSocketEvent> = flowOf(),
) : WebSocketClient {
    override fun connect(url: String): Flow<WebSocketEvent> = events
}
