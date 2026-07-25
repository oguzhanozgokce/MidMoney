package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

import app.oguzhanozgokce.midmoney.feature.watchlist.analytics.WatchlistAnalyticsEvent
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.NavigationCommand
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.FavoritesRepository
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import app.oguzhanozgokce.midmoney.testing.FakeAnalytics
import app.oguzhanozgokce.midmoney.testing.FakeNavigator
import app.oguzhanozgokce.midmoney.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val navigator = FakeNavigator()
    private val analytics = FakeAnalytics()

    private fun viewModel(
        marketRepository: MarketRepository = FakeMarketRepository(),
        favorites: Set<String> = emptySet(),
    ) = WatchlistViewModel(
        marketClient = MarketClient(marketRepository, FakeFavoritesRepository(favorites)),
        navigator = navigator,
        analytics = analytics,
    )

    @Test
    fun `tracks the viewed event on init`() = runTest {
        viewModel()

        assertThat(analytics.trackedEvents).contains(WatchlistAnalyticsEvent.Viewed)
    }

    @Test
    fun `no favorites leaves the list empty without fetching quotes`() = runTest {
        val repository = FakeMarketRepository()

        val viewModel = viewModel(repository)

        val state = viewModel.currentUiState
        assertThat(state.items).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.isEmpty).isTrue()
        assertThat(repository.requestedSymbols).isEmpty()
    }

    @Test
    fun `favorited symbols are fetched and mapped into the list`() = runTest {
        val repository = FakeMarketRepository(quotes = listOf(quote("AAPL"), quote("TSLA")))

        val viewModel = viewModel(repository, favorites = setOf("AAPL", "TSLA"))

        val state = viewModel.currentUiState
        assertThat(state.items.map { it.symbol }).containsExactly("AAPL", "TSLA")
        assertThat(state.isLoading).isFalse()
        assertThat(state.isEmpty).isFalse()
        assertThat(repository.requestedSymbols).containsExactly("AAPL", "TSLA")
    }

    @Test
    fun `a failed fetch surfaces an error message`() = runTest {
        val viewModel = viewModel(
            FakeMarketRepository(error = RuntimeException("boom")),
            favorites = setOf("AAPL"),
        )

        val state = viewModel.currentUiState
        assertThat(state.errorMessage).isNotNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.isEmpty).isFalse()
    }

    @Test
    fun `open detail navigates to the symbol and tracks the event`() = runTest {
        val viewModel = viewModel()

        viewModel.onAction(WatchlistUiAction.OpenDetail("AAPL"))

        assertThat(navigator.commandsLog).contains(NavigationCommand.Navigate(Destination.Detail("AAPL")))
        assertThat(analytics.trackedEvents).contains(WatchlistAnalyticsEvent.OpenDetail("AAPL"))
    }

    @Test
    fun `retry fetches the favorites again`() = runTest {
        val repository = FakeMarketRepository(quotes = listOf(quote("AAPL")))
        val viewModel = viewModel(repository, favorites = setOf("AAPL"))

        viewModel.onAction(WatchlistUiAction.Retry)

        assertThat(repository.fetchCount).isEqualTo(2)
    }

    private fun quote(symbol: String) = Quote(
        symbol = symbol,
        current = 150.0,
        change = 1.0,
        percentChange = 0.5,
        high = 151.0,
        low = 149.0,
        open = 150.0,
        previousClose = 149.5,
    )
}

private class FakeMarketRepository(
    private val quotes: List<Quote> = emptyList(),
    private val error: Throwable? = null,
) : MarketRepository {
    val requestedSymbols: MutableList<String> = mutableListOf()
    var fetchCount: Int = 0
        private set

    override suspend fun getQuotes(symbols: List<String>): Result<List<Quote>> {
        fetchCount++
        requestedSymbols += symbols
        return error?.let { Result.failure(it) } ?: Result.success(quotes)
    }

    override suspend fun getQuote(symbol: String): Result<Quote> =
        error?.let { Result.failure(it) } ?: Result.success(quotes.first())

    override suspend fun searchSymbols(query: String): Result<List<SymbolMatch>> =
        Result.success(emptyList())

    override fun observePrice(symbol: String): Flow<Double> = emptyFlow()
}

private class FakeFavoritesRepository(private val favorites: Set<String>) : FavoritesRepository {
    override val favoriteSymbols: Flow<Set<String>> = flowOf(favorites)
    override suspend fun toggle(symbol: String) = Unit
}
