package app.oguzhanozgokce.midmoney.feature.marketlist.presentation

import app.oguzhanozgokce.midmoney.feature.marketlist.analytics.MarketListAnalyticsEvent
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.NavigationCommand
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter
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
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/** Shorter than the ViewModel's 350 ms debounce, so a pending search is still waiting. */
private const val WITHIN_DEBOUNCE_MS = 100L

@OptIn(ExperimentalCoroutinesApi::class)
class MarketListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val navigator = FakeNavigator()
    private val analytics = FakeAnalytics()

    private fun viewModel(marketRepository: MarketRepository) = MarketListViewModel(
        marketClient = MarketClient(marketRepository, FakeFavoritesRepository()),
        navigator = navigator,
        analytics = analytics,
    )

    @Test
    fun `loads quotes and tracks the viewed event on init`() = runTest {
        val viewModel = viewModel(FakeMarketRepository(quotes = listOf(quote("AAPL"))))

        val state = viewModel.currentUiState
        assertThat(state.isLoading).isFalse()
        assertThat(state.quotes.map { it.symbol }).containsExactly("AAPL")
        assertThat(analytics.trackedEvents).contains(MarketListAnalyticsEvent.Viewed)
    }

    @Test
    fun `a failed load surfaces an error message`() = runTest {
        val viewModel = viewModel(FakeMarketRepository(error = RuntimeException("boom")))

        val state = viewModel.currentUiState
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNotNull()
    }

    @Test
    fun `open detail navigates to the symbol and tracks the event`() = runTest {
        val viewModel = viewModel(FakeMarketRepository())

        viewModel.onAction(MarketListUiAction.OpenDetail("AAPL"))

        assertThat(navigator.commandsLog).contains(NavigationCommand.Navigate(Destination.Detail("AAPL")))
        assertThat(analytics.trackedEvents).contains(MarketListAnalyticsEvent.OpenDetail("AAPL"))
    }

    @Test
    fun `back goes back`() = runTest {
        val viewModel = viewModel(FakeMarketRepository())

        viewModel.onAction(MarketListUiAction.BackClicked)

        assertThat(navigator.commandsLog).contains(NavigationCommand.Back)
    }

    @Test
    fun `the gainers filter sorts quotes by daily change descending and tracks the filter`() = runTest {
        val viewModel = viewModel(
            FakeMarketRepository(
                quotes = listOf(
                    quote("AAA", percentChange = -2.0),
                    quote("BBB", percentChange = 5.0),
                    quote("CCC", percentChange = 1.0),
                ),
            ),
        )

        viewModel.onAction(MarketListUiAction.SelectFilter(MarketFilter.Gainers))

        assertThat(viewModel.currentUiState.quotes.map { it.symbol })
            .containsExactly("BBB", "CCC", "AAA")
            .inOrder()
        assertThat(analytics.trackedEvents)
            .contains(MarketListAnalyticsEvent.FilterSelected(MarketFilter.Gainers.name))
    }

    @Test
    fun `a query is searched only after the debounce elapses`() = runTest {
        val repository = FakeMarketRepository(matches = listOf(SymbolMatch("AAPL", "Apple Inc.")))
        val viewModel = viewModel(repository)

        viewModel.onAction(MarketListUiAction.QueryChanged("AA"))
        advanceTimeBy(WITHIN_DEBOUNCE_MS)
        assertThat(repository.searchQueries).isEmpty()

        advanceUntilIdle()

        assertThat(repository.searchQueries).containsExactly("AA")
        assertThat(viewModel.currentUiState.results.map { it.symbol }).containsExactly("AAPL")
        assertThat(viewModel.currentUiState.isSearching).isFalse()
        assertThat(analytics.trackedEvents).contains(MarketListAnalyticsEvent.Searched("AA"))
    }

    @Test
    fun `typing again cancels the pending search so only the last query is sent`() = runTest {
        val repository = FakeMarketRepository(matches = listOf(SymbolMatch("AAPL", "Apple Inc.")))
        val viewModel = viewModel(repository)

        viewModel.onAction(MarketListUiAction.QueryChanged("A"))
        advanceTimeBy(WITHIN_DEBOUNCE_MS)
        viewModel.onAction(MarketListUiAction.QueryChanged("AA"))
        advanceTimeBy(WITHIN_DEBOUNCE_MS)
        viewModel.onAction(MarketListUiAction.QueryChanged("AAPL"))
        advanceUntilIdle()

        assertThat(repository.searchQueries).containsExactly("AAPL")
    }

    @Test
    fun `a blank query clears the previous results without searching`() = runTest {
        val repository = FakeMarketRepository(matches = listOf(SymbolMatch("AAPL", "Apple Inc.")))
        val viewModel = viewModel(repository)
        viewModel.onAction(MarketListUiAction.QueryChanged("AA"))
        advanceUntilIdle()

        viewModel.onAction(MarketListUiAction.QueryChanged(""))
        advanceUntilIdle()

        val state = viewModel.currentUiState
        assertThat(state.results).isEmpty()
        assertThat(state.isSearching).isFalse()
        assertThat(state.isSearchActive).isFalse()
        assertThat(repository.searchQueries).containsExactly("AA")
    }

    private fun quote(symbol: String, percentChange: Double = 0.5) = Quote(
        symbol = symbol,
        current = 150.0,
        change = 1.0,
        percentChange = percentChange,
        high = 151.0,
        low = 149.0,
        open = 150.0,
        previousClose = 149.5,
    )
}

private class FakeMarketRepository(
    private val quotes: List<Quote> = emptyList(),
    private val matches: List<SymbolMatch> = emptyList(),
    private val error: Throwable? = null,
) : MarketRepository {
    val searchQueries: MutableList<String> = mutableListOf()

    override suspend fun getQuotes(symbols: List<String>): Result<List<Quote>> =
        error?.let { Result.failure(it) } ?: Result.success(quotes)

    override suspend fun getQuote(symbol: String): Result<Quote> =
        error?.let { Result.failure(it) } ?: Result.success(quotes.first())

    override suspend fun searchSymbols(query: String): Result<List<SymbolMatch>> {
        searchQueries += query
        return error?.let { Result.failure(it) } ?: Result.success(matches)
    }

    override fun observePrice(symbol: String): Flow<Double> = emptyFlow()
}

private class FakeFavoritesRepository : FavoritesRepository {
    override val favoriteSymbols: Flow<Set<String>> = flowOf(emptySet())
    override suspend fun toggle(symbol: String) = Unit
}
