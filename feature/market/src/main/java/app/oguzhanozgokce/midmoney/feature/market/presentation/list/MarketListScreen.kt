package app.oguzhanozgokce.midmoney.feature.market.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButton
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButtonStyle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyEmptyState
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyLoading
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyTextField
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyTopAppBar
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.feature.market.presentation.component.MarketFilters
import app.oguzhanozgokce.midmoney.feature.market.presentation.component.QuoteListItem
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
                title = "Markets",
                onNavigationClick = { onAction(MarketListUiAction.BackClicked) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            MidMoneyTextField(
                value = uiState.query,
                onValueChange = { onAction(MarketListUiAction.QueryChanged(it)) },
                label = "Search",
                placeholder = "Search stocks (e.g. AAPL)",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            if (uiState.isSearchActive) {
                SearchResults(uiState = uiState, onAction = onAction)
            } else {
                MarketFilters(
                    selected = uiState.selectedFilter,
                    onSelect = { onAction(MarketListUiAction.SelectFilter(it)) },
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
            message = uiState.errorMessage,
            onRetry = { onAction(MarketListUiAction.Retry) },
        )
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            items(uiState.quotes, key = { it.symbol }) { quote ->
                QuoteListItem(
                    quote = quote,
                    onClick = { onAction(MarketListUiAction.OpenDetail(quote.symbol)) },
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
            title = "No results",
            description = "No stocks match \"${uiState.query}\".",
        )
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            items(uiState.results, key = { it.symbol }) { match ->
                SearchResultItem(
                    match = match,
                    onClick = { onAction(MarketListUiAction.OpenDetail(match.symbol)) },
                )
            }
        }
    }
}

@Composable
private fun SearchResultItem(match: SymbolMatch, onClick: () -> Unit) {
    Column(
        modifier = Modifier
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
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        MidMoneyButton(text = "Retry", onClick = onRetry, style = MidMoneyButtonStyle.Outlined)
    }
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
