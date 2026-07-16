package app.oguzhanozgokce.midmoney.feature.market.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButton
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButtonStyle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyLoading
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScreenHeader
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.feature.market.presentation.component.HomeBannerPager
import app.oguzhanozgokce.midmoney.feature.market.presentation.component.QuoteListItem
import app.oguzhanozgokce.midmoney.feature.market.presentation.model.HomeBannerUi

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
                .fillMaxSize()
                .padding(padding),
        ) {
            MidMoneyScreenHeader(title = "MidMoney")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                item {
                    HomeBannerPager(
                        banners = HomeBannerUi.defaults,
                        onActionClick = { onOpenWatchlist() },
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
                item {
                    Text(
                        text = "Popular",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }

                when {
                    uiState.isLoading -> item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                        ) {
                            MidMoneyLoading()
                        }
                    }

                    uiState.errorMessage != null -> item {
                        ErrorContent(
                            message = uiState.errorMessage,
                            onRetry = { onAction(MarketUiAction.Retry) },
                        )
                    }

                    else -> items(uiState.quotes, key = { it.symbol }) { quote ->
                        QuoteListItem(
                            quote = quote,
                            onClick = { onAction(MarketUiAction.OpenDetail(quote.symbol)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
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
private fun MarketScreenPreview(
    @PreviewParameter(MarketUiStatePreviewProvider::class) state: MarketUiState,
) {
    MidMoneyTheme {
        MarketScreen(uiState = state, onAction = {}, onOpenWatchlist = {})
    }
}
