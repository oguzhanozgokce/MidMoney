package app.oguzhanozgokce.midmoney.feature.market.presentation

import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.event.AnalyticsEvent
import app.oguzhanozgokce.midmoney.event.EventSupplier
import app.oguzhanozgokce.midmoney.feature.market.analytics.MarketAnalyticsEvent
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.NavigationCommand
import app.oguzhanozgokce.midmoney.navigation.Navigator
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.FavoritesRepository
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarketViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val navigator = FakeNavigator()
    private val analytics = FakeAnalytics()

    private fun viewModel(marketRepository: MarketRepository) = MarketViewModel(
        marketClient = MarketClient(marketRepository, FakeFavoritesRepository()),
        navigator = navigator,
        analytics = analytics,
    )

    @Test
    fun `loads quotes on init`() = runTest {
        val viewModel = viewModel(FakeMarketRepository(quotes = listOf(quote("AAPL"))))

        val state = viewModel.currentUiState
        assertThat(state.isLoading).isFalse()
        assertThat(state.quotes.map { it.symbol }).containsExactly("AAPL")
    }

    @Test
    fun `sets error message when loading fails`() = runTest {
        val viewModel = viewModel(FakeMarketRepository(error = RuntimeException("boom")))

        val state = viewModel.currentUiState
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNotNull()
    }

    @Test
    fun `open detail navigates to detail and tracks the event`() = runTest {
        val viewModel = viewModel(FakeMarketRepository())

        viewModel.onAction(MarketUiAction.OpenDetail("AAPL"))

        assertThat(navigator.commandsLog).contains(NavigationCommand.Navigate(Destination.Detail("AAPL")))
        assertThat(analytics.trackedEvents).contains(MarketAnalyticsEvent.OpenDetail("AAPL"))
    }

    @Test
    fun `tracks the viewed event on init`() = runTest {
        viewModel(FakeMarketRepository())

        assertThat(analytics.trackedEvents).contains(MarketAnalyticsEvent.Viewed)
    }

    @Test
    fun `selecting a filter tracks the filter event`() = runTest {
        val viewModel = viewModel(FakeMarketRepository())

        viewModel.onAction(MarketUiAction.SelectFilter(MarketFilter.Gainers))

        assertThat(analytics.trackedEvents)
            .contains(MarketAnalyticsEvent.FilterSelected(MarketFilter.Gainers.name))
    }

    @Test
    fun `open all navigates to the list and tracks see all`() = runTest {
        val viewModel = viewModel(FakeMarketRepository())

        viewModel.onAction(MarketUiAction.OpenAll)

        assertThat(navigator.commandsLog).contains(NavigationCommand.Navigate(Destination.MarketList))
        assertThat(analytics.trackedEvents).contains(MarketAnalyticsEvent.SeeAllClicked)
    }

    @Test
    fun `gainers filter sorts quotes by daily change descending`() = runTest {
        val viewModel = viewModel(
            FakeMarketRepository(
                quotes = listOf(
                    quote("AAA", percentChange = -2.0),
                    quote("BBB", percentChange = 5.0),
                    quote("CCC", percentChange = 1.0),
                ),
            ),
        )

        viewModel.onAction(MarketUiAction.SelectFilter(MarketFilter.Gainers))

        assertThat(viewModel.currentUiState.quotes.map { it.symbol })
            .containsExactly("BBB", "CCC", "AAA")
            .inOrder()
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
    private val error: Throwable? = null,
) : MarketRepository {
    override suspend fun getQuotes(symbols: List<String>): Result<List<Quote>> =
        error?.let { Result.failure(it) } ?: Result.success(quotes)

    override suspend fun getQuote(symbol: String): Result<Quote> =
        error?.let { Result.failure(it) } ?: Result.success(quotes.first())

    override suspend fun searchSymbols(query: String): Result<List<SymbolMatch>> =
        Result.success(emptyList())

    override fun observePrice(symbol: String): Flow<Double> = emptyFlow()
}

private class FakeFavoritesRepository : FavoritesRepository {
    override val favoriteSymbols: Flow<Set<String>> = flowOf(emptySet())
    override suspend fun toggle(symbol: String) = Unit
}

private class FakeNavigator : Navigator {
    val commandsLog: MutableList<NavigationCommand> = mutableListOf()

    override val commands: Flow<NavigationCommand> = emptyFlow()
    override fun navigate(destination: Destination) {
        commandsLog += NavigationCommand.Navigate(destination)
    }

    override fun navigateAndClearBackStack(destination: Destination) {
        commandsLog += NavigationCommand.NavigateAndClearBackStack(destination)
    }

    override fun goBack() {
        commandsLog += NavigationCommand.Back
    }
}

private class FakeAnalytics : Analytics {
    val trackedEvents: MutableList<AnalyticsEvent> = mutableListOf()

    override fun track(event: AnalyticsEvent, vararg suppliers: EventSupplier) {
        trackedEvents += event
    }

    override fun setUserId(id: String?) = Unit
}
