package app.oguzhanozgokce.midmoney.feature.detail.presentation

import app.cash.turbine.test
import app.oguzhanozgokce.midmoney.feature.detail.analytics.DetailAnalyticsEvent
import app.oguzhanozgokce.midmoney.navigation.NavigationCommand
import app.oguzhanozgokce.midmoney.plugin.market.MarketClient
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.FavoritesRepository
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import app.oguzhanozgokce.midmoney.plugin.news.NewsClient
import app.oguzhanozgokce.midmoney.plugin.news.domain.model.NewsArticle
import app.oguzhanozgokce.midmoney.plugin.news.domain.repository.NewsRepository
import app.oguzhanozgokce.midmoney.testing.FakeAnalytics
import app.oguzhanozgokce.midmoney.testing.FakeNavigator
import app.oguzhanozgokce.midmoney.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val navigator = FakeNavigator()
    private val analytics = FakeAnalytics()

    private fun viewModel(
        marketRepository: MarketRepository = FakeMarketRepository(),
        favoritesRepository: FavoritesRepository = FakeFavoritesRepository(),
        newsRepository: NewsRepository = FakeNewsRepository(),
    ) = DetailViewModel(
        marketClient = MarketClient(marketRepository, favoritesRepository),
        newsClient = NewsClient(newsRepository),
        navigator = navigator,
        analytics = analytics,
    )

    @Test
    fun `load fills in the symbol, the local company name and the quote`() = runTest {
        val viewModel = viewModel()

        viewModel.onAction(DetailUiAction.Load("AAPL"))

        val state = viewModel.currentUiState
        assertThat(state.symbol).isEqualTo("AAPL")
        assertThat(state.name).isEqualTo("Apple Inc.")
        assertThat(state.quote).isNotNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
        assertThat(analytics.trackedEvents).contains(DetailAnalyticsEvent.Viewed("AAPL"))
    }

    @Test
    fun `a failed quote surfaces an error message`() = runTest {
        val viewModel = viewModel(FakeMarketRepository(error = RuntimeException("boom")))

        viewModel.onAction(DetailUiAction.Load("AAPL"))

        val state = viewModel.currentUiState
        assertThat(state.errorMessage).isNotNull()
        assertThat(state.quote).isNull()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `load also fetches the company news`() = runTest {
        val viewModel = viewModel(newsRepository = FakeNewsRepository(articles = listOf(article())))

        viewModel.onAction(DetailUiAction.Load("AAPL"))

        val state = viewModel.currentUiState
        assertThat(state.news.map { it.headline }).containsExactly("Apple ships something")
        assertThat(state.isNewsLoading).isFalse()
    }

    @Test
    fun `failing news leaves the screen usable`() = runTest {
        val viewModel = viewModel(newsRepository = FakeNewsRepository(error = RuntimeException("boom")))

        viewModel.onAction(DetailUiAction.Load("AAPL"))

        val state = viewModel.currentUiState
        assertThat(state.news).isEmpty()
        assertThat(state.isNewsLoading).isFalse()
        assertThat(state.quote).isNotNull()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `a streamed price overrides the displayed price`() = runTest {
        val viewModel = viewModel(FakeMarketRepository(livePrices = listOf(151.25)))

        viewModel.onAction(DetailUiAction.Load("AAPL"))

        assertThat(viewModel.currentUiState.livePriceText).isNotNull()
    }

    @Test
    fun `a stream error is ignored so the REST quote stays on screen`() = runTest {
        val viewModel = viewModel(FakeMarketRepository(streamError = RuntimeException("socket down")))

        viewModel.onAction(DetailUiAction.Load("AAPL"))

        val state = viewModel.currentUiState
        assertThat(state.livePriceText).isNull()
        assertThat(state.quote).isNotNull()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `back goes back`() = runTest {
        val viewModel = viewModel()

        viewModel.onAction(DetailUiAction.BackClicked)

        assertThat(navigator.commandsLog).contains(NavigationCommand.Back)
    }

    @Test
    fun `buy shows a message and tracks the buy intent`() = runTest {
        val viewModel = viewModel()
        viewModel.onAction(DetailUiAction.Load("AAPL"))

        viewModel.uiEffect.test {
            viewModel.onAction(DetailUiAction.BuyClicked)
            assertThat(awaitItem()).isInstanceOf(DetailUiEffect.ShowMessage::class.java)
        }

        assertThat(analytics.trackedEvents).contains(DetailAnalyticsEvent.Buy("AAPL"))
    }

    @Test
    fun `sell shows a message and tracks the sell intent`() = runTest {
        val viewModel = viewModel()
        viewModel.onAction(DetailUiAction.Load("AAPL"))

        viewModel.uiEffect.test {
            viewModel.onAction(DetailUiAction.SellClicked)
            assertThat(awaitItem()).isInstanceOf(DetailUiEffect.ShowMessage::class.java)
        }

        assertThat(analytics.trackedEvents).contains(DetailAnalyticsEvent.Sell("AAPL"))
    }

    @Test
    fun `toggling save favorites the symbol and reflects it in the state`() = runTest {
        val favorites = FakeFavoritesRepository()
        val viewModel = viewModel(favoritesRepository = favorites)
        viewModel.onAction(DetailUiAction.Load("AAPL"))

        viewModel.uiEffect.test {
            viewModel.onAction(DetailUiAction.ToggleSave)
            assertThat(awaitItem()).isInstanceOf(DetailUiEffect.ShowMessage::class.java)
        }

        assertThat(viewModel.currentUiState.isSaved).isTrue()
        assertThat(analytics.trackedEvents).contains(DetailAnalyticsEvent.Save("AAPL", true))
    }

    @Test
    fun `toggling save on an already saved symbol unfavorites it`() = runTest {
        val favorites = FakeFavoritesRepository(initial = setOf("AAPL"))
        val viewModel = viewModel(favoritesRepository = favorites)
        viewModel.onAction(DetailUiAction.Load("AAPL"))
        assertThat(viewModel.currentUiState.isSaved).isTrue()

        viewModel.uiEffect.test {
            viewModel.onAction(DetailUiAction.ToggleSave)
            awaitItem()
        }

        assertThat(viewModel.currentUiState.isSaved).isFalse()
        assertThat(analytics.trackedEvents).contains(DetailAnalyticsEvent.Save("AAPL", false))
    }

    @Test
    fun `save is ignored before a symbol is loaded`() = runTest {
        val favorites = FakeFavoritesRepository()
        val viewModel = viewModel(favoritesRepository = favorites)

        viewModel.onAction(DetailUiAction.ToggleSave)

        assertThat(favorites.toggled).isEmpty()
    }

    @Test
    fun `tapping a news item opens its url and tracks it`() = runTest {
        val viewModel = viewModel()
        viewModel.onAction(DetailUiAction.Load("AAPL"))

        viewModel.uiEffect.test {
            viewModel.onAction(DetailUiAction.NewsClicked("https://news.test/1"))
            assertThat(awaitItem()).isEqualTo(DetailUiEffect.OpenUrl("https://news.test/1"))
        }

        assertThat(analytics.trackedEvents).contains(DetailAnalyticsEvent.NewsOpened("AAPL"))
    }

    @Test
    fun `retry reloads the current symbol`() = runTest {
        val repository = FakeMarketRepository()
        val viewModel = viewModel(repository)
        viewModel.onAction(DetailUiAction.Load("AAPL"))

        viewModel.onAction(DetailUiAction.Retry)

        assertThat(repository.requestedQuotes).containsExactly("AAPL", "AAPL")
        assertThat(analytics.trackedEvents).contains(DetailAnalyticsEvent.Retry("AAPL"))
    }

    private fun article() = NewsArticle(
        headline = "Apple ships something",
        source = "Reuters",
        url = "https://news.test/1",
        imageUrl = "https://img.test/1.png",
        publishedEpochSeconds = 1_700_000_000L,
    )
}

private class FakeMarketRepository(
    private val error: Throwable? = null,
    private val livePrices: List<Double> = emptyList(),
    private val streamError: Throwable? = null,
) : MarketRepository {
    val requestedQuotes: MutableList<String> = mutableListOf()

    override suspend fun getQuotes(symbols: List<String>): Result<List<Quote>> =
        error?.let { Result.failure(it) } ?: Result.success(emptyList())

    override suspend fun getQuote(symbol: String): Result<Quote> {
        requestedQuotes += symbol
        return error?.let { Result.failure(it) } ?: Result.success(quote(symbol))
    }

    override suspend fun searchSymbols(query: String): Result<List<SymbolMatch>> =
        Result.success(emptyList())

    override fun observePrice(symbol: String): Flow<Double> = when {
        streamError != null -> flow { throw streamError }
        livePrices.isEmpty() -> emptyFlow()
        else -> livePrices.asFlowOfDoubles()
    }

    private fun List<Double>.asFlowOfDoubles(): Flow<Double> = flow { forEach { emit(it) } }

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

private class FakeFavoritesRepository(initial: Set<String> = emptySet()) : FavoritesRepository {
    private val state = MutableStateFlow(initial)
    val toggled: MutableList<String> = mutableListOf()

    override val favoriteSymbols: Flow<Set<String>> = state

    override suspend fun toggle(symbol: String) {
        toggled += symbol
        state.update { if (symbol in it) it - symbol else it + symbol }
    }
}

private class FakeNewsRepository(
    private val articles: List<NewsArticle> = emptyList(),
    private val error: Throwable? = null,
) : NewsRepository {
    override suspend fun getCompanyNews(symbol: String): Result<List<NewsArticle>> =
        error?.let { Result.failure(it) } ?: Result.success(articles)

    override suspend fun getMarketNews(): Result<List<NewsArticle>> =
        error?.let { Result.failure(it) } ?: Result.success(articles)
}
