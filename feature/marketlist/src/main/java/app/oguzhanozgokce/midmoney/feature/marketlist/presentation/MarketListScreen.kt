package app.oguzhanozgokce.midmoney.feature.marketlist.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyEmptyState
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyFilterChip
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyFilterChips
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyLoading
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyQuoteRow
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneySearchBar
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyTopAppBar
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.feature.marketlist.R
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.MarketFilter
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch

@Composable
fun MarketListRoute(viewModel: MarketListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MarketListScreen(uiState = uiState, onAction = viewModel::onAction)
}

@Composable
private fun MarketListScreen(
    uiState: MarketListUiState,
    onAction: (MarketListUiAction) -> Unit,
) {
    MidMoneyScaffold(
        topBar = {
            MidMoneyTopAppBar(
                title = stringResource(R.string.markets_title),
                onNavigationClick = { onAction(MarketListUiAction.BackClicked) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .testTag(MarketListTestTags.SCREEN)
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            MidMoneySearchBar(
                query = uiState.query,
                onQueryChange = { onAction(MarketListUiAction.QueryChanged(it)) },
                placeholder = stringResource(R.string.markets_search_hint),
                modifier = Modifier
                    .testTag(MarketListTestTags.SEARCH)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            if (uiState.isSearchActive) {
                SearchResults(uiState = uiState, onAction = onAction)
            } else {
                MidMoneyFilterChips(
                    chips = MarketFilter.entries.map {
                        MidMoneyFilterChip(
                            label = stringResource(it.labelRes()),
                            testTag = MarketListTestTags.filter(it),
                        )
                    },
                    selectedIndex = uiState.selectedFilter.ordinal,
                    onSelect = { onAction(MarketListUiAction.SelectFilter(MarketFilter.entries[it])) },
                )
                QuoteList(uiState = uiState, onAction = onAction)
            }
        }
    }
}

@Composable
private fun QuoteList(
    uiState: MarketListUiState,
    onAction: (MarketListUiAction) -> Unit,
) {
    when {
        uiState.isLoading -> CenteredLoading()
        uiState.errorMessage != null -> ErrorContent(
            errorText = uiState.errorMessage,
            onRetry = { onAction(MarketListUiAction.Retry) },
        )
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
        ) {
            items(uiState.quotes, key = { it.symbol }) { quote ->
                MidMoneyQuoteRow(
                    symbol = quote.symbol,
                    name = quote.name,
                    priceText = quote.priceText,
                    changeText = quote.changePercentText,
                    isPositive = quote.isPositive,
                    onClick = { onAction(MarketListUiAction.OpenDetail(quote.symbol)) },
                    modifier = Modifier.testTag(MarketListTestTags.quote(quote.symbol)),
                )
            }
        }
    }
}

@Composable
private fun SearchResults(
    uiState: MarketListUiState,
    onAction: (MarketListUiAction) -> Unit,
) {
    when {
        uiState.isSearching -> CenteredLoading()
        uiState.results.isEmpty() -> MidMoneyEmptyState(
            icon = Icons.Outlined.Search,
            title = stringResource(R.string.markets_no_results_title),
            description = stringResource(R.string.markets_no_results_description, uiState.query),
            modifier = Modifier.testTag(MarketListTestTags.EMPTY),
        )
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
        ) {
            items(uiState.results, key = { it.symbol }) { match ->
                SearchResultItem(
                    match = match,
                    onClick = { onAction(MarketListUiAction.OpenDetail(match.symbol)) },
                    modifier = Modifier.testTag(MarketListTestTags.result(match.symbol)),
                )
            }
        }
    }
}

@Composable
private fun SearchResultItem(match: SymbolMatch, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = match.symbol,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = match.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CenteredLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MidMoneyLoading()
    }
}

@Composable
private fun ErrorContent(errorText: UiText, onRetry: () -> Unit) {
    MidMoneyEmptyState(
        icon = Icons.Outlined.CloudOff,
        title = stringResource(R.string.markets_error_title),
        description = errorText.asString(),
        actionText = stringResource(R.string.markets_retry),
        onActionClick = onRetry,
        actionTestTag = MarketListTestTags.RETRY,
    )
}

@StringRes
private fun MarketFilter.labelRes(): Int = when (this) {
    MarketFilter.Popular -> R.string.filter_popular
    MarketFilter.Gainers -> R.string.filter_gainers
    MarketFilter.Losers -> R.string.filter_losers
}

@PreviewLightDark
@Composable
private fun MarketListScreenPreview(
    @PreviewParameter(MarketListUiStatePreviewProvider::class) state: MarketListUiState,
) {
    MidMoneyTheme {
        MarketListScreen(uiState = state, onAction = {})
    }
}
