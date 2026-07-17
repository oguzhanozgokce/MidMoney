package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyEmptyState
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyLoading
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyQuoteRow
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScreenHeader
import app.oguzhanozgokce.midmoney.designsystem.text.UiText
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.feature.watchlist.R

@Composable
fun WatchlistRoute(viewModel: WatchlistViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WatchlistScreen(uiState = uiState, onAction = viewModel::onAction)
}

@Composable
private fun WatchlistScreen(
    uiState: WatchlistUiState,
    onAction: (WatchlistUiAction) -> Unit,
) {
    MidMoneyScaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            MidMoneyScreenHeader(title = stringResource(R.string.watchlist_title))
            WatchlistContent(uiState = uiState, onAction = onAction)
        }
    }
}

@Composable
private fun WatchlistContent(
    uiState: WatchlistUiState,
    onAction: (WatchlistUiAction) -> Unit,
) {
    when {
        uiState.isLoading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            MidMoneyLoading()
        }

        uiState.errorMessage != null -> ErrorContent(
            errorText = uiState.errorMessage,
            onRetry = { onAction(WatchlistUiAction.Retry) },
        )

        uiState.isEmpty -> MidMoneyEmptyState(
            icon = Icons.Outlined.Star,
            title = stringResource(R.string.watchlist_empty_title),
            description = stringResource(R.string.watchlist_empty_description),
            modifier = Modifier.fillMaxSize(),
        )

        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
        ) {
            items(uiState.items, key = { it.symbol }) { quote ->
                MidMoneyQuoteRow(
                    symbol = quote.symbol,
                    name = quote.name,
                    priceText = quote.priceText,
                    changeText = quote.changePercentText,
                    isPositive = quote.isPositive,
                    onClick = { onAction(WatchlistUiAction.OpenDetail(quote.symbol)) },
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(errorText: UiText, onRetry: () -> Unit) {
    MidMoneyEmptyState(
        icon = Icons.Outlined.CloudOff,
        title = stringResource(R.string.watchlist_error_title),
        description = errorText.asString(),
        actionText = stringResource(R.string.watchlist_retry),
        onActionClick = onRetry,
        modifier = Modifier.fillMaxSize(),
    )
}

@PreviewLightDark
@Composable
private fun WatchlistScreenPreview(
    @PreviewParameter(WatchlistUiStatePreviewProvider::class) state: WatchlistUiState,
) {
    MidMoneyTheme {
        WatchlistScreen(uiState = state, onAction = {})
    }
}
