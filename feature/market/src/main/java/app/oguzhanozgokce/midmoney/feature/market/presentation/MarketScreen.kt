package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButton
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButtonStyle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyFilterChips
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyLoading
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyQuoteRow
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScreenHeader
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.feature.market.R
import app.oguzhanozgokce.midmoney.feature.market.presentation.component.MarketBannerPager
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.MarketBannerAction
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter

private const val HOME_PREVIEW_COUNT = 6

@Composable
fun MarketRoute(
    onOpenWatchlist: () -> Unit,
    viewModel: MarketViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MarketScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        onOpenWatchlist = onOpenWatchlist,
    )
}

@Composable
private fun MarketScreen(
    uiState: MarketUiState,
    onAction: (MarketUiAction) -> Unit,
    onOpenWatchlist: () -> Unit,
) {
    MidMoneyScaffold { padding ->
        Column(
            modifier = Modifier
                .testTag(MarketTestTags.SCREEN)
                .fillMaxSize()
                .padding(padding),
        ) {
            MidMoneyScreenHeader(title = "MidMoney")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                item {
                    MarketBannerPager(
                        banners = uiState.banners,
                        onActionClick = { banner ->
                            when (banner.action) {
                                MarketBannerAction.OpenMarketList -> onAction(MarketUiAction.OpenAll)
                                MarketBannerAction.OpenWatchlist -> onOpenWatchlist()
                            }
                        },
                        modifier = Modifier
                            .testTag(MarketTestTags.BANNER)
                            .padding(vertical = 8.dp),
                    )
                }
                item {
                    SectionHeader(
                        title = stringResource(R.string.market_section_title),
                        onSeeAll = { onAction(MarketUiAction.OpenAll) },
                    )
                }
                item {
                    MidMoneyFilterChips(
                        options = MarketFilter.entries.map { stringResource(it.labelRes()) },
                        selectedIndex = uiState.selectedFilter.ordinal,
                        onSelect = { onAction(MarketUiAction.SelectFilter(MarketFilter.entries[it])) },
                        optionTestTags = MarketFilter.entries.map { MarketTestTags.filter(it) },
                    )
                }

                when {
                    uiState.isLoading -> item {
                        Box(
                            modifier = Modifier
                                .testTag(MarketTestTags.LOADING)
                                .fillMaxWidth()
                                .height(240.dp),
                        ) {
                            MidMoneyLoading()
                        }
                    }

                    uiState.errorMessage != null -> item {
                        ErrorContent(
                            errorText = uiState.errorMessage,
                            onRetry = { onAction(MarketUiAction.Retry) },
                        )
                    }

                    else -> items(uiState.quotes.take(HOME_PREVIEW_COUNT), key = { it.symbol }) { quote ->
                        MidMoneyQuoteRow(
                            symbol = quote.symbol,
                            name = quote.name,
                            priceText = quote.priceText,
                            changeText = quote.changePercentText,
                            isPositive = quote.isPositive,
                            onClick = { onAction(MarketUiAction.OpenDetail(quote.symbol)) },
                            modifier = Modifier.testTag(MarketTestTags.quote(quote.symbol)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = onSeeAll, modifier = Modifier.testTag(MarketTestTags.SEE_ALL)) {
            Text(text = stringResource(R.string.market_see_all))
        }
    }
}

@StringRes
private fun MarketFilter.labelRes(): Int = when (this) {
    MarketFilter.Popular -> R.string.filter_popular
    MarketFilter.Gainers -> R.string.filter_gainers
    MarketFilter.Losers -> R.string.filter_losers
}

@Composable
private fun ErrorContent(errorText: UiText, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = errorText.asString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        MidMoneyButton(
            text = stringResource(R.string.market_retry),
            onClick = onRetry,
            style = MidMoneyButtonStyle.Outlined,
            modifier = Modifier.testTag(MarketTestTags.RETRY),
        )
    }
}

@PreviewLightDark
@Composable
private fun MarketScreenPreview(
    @PreviewParameter(MarketUiStatePreviewProvider::class) state: MarketUiState,
) {
    MidMoneyTheme {
        MarketScreen(uiState = state, onAction = {}, onOpenWatchlist = {})
    }
}
