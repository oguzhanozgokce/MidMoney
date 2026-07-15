package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyEmptyState
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyTopAppBar
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme

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
    MidMoneyScaffold(
        topBar = {
            MidMoneyTopAppBar(
                title = "Watchlist",
                onNavigationClick = { onAction(WatchlistUiAction.BackClicked) },
            )
        },
    ) { padding ->
        MidMoneyEmptyState(
            icon = Icons.Outlined.Star,
            title = "Your watchlist is empty",
            description = "Symbols you follow will appear here.",
            modifier = Modifier.padding(padding),
        )
    }
}

@PreviewLightDark
@Composable
private fun WatchlistScreenPreview() {
    MidMoneyTheme {
        WatchlistScreen(uiState = WatchlistUiState(), onAction = {})
    }
}
