package app.oguzhanozgokce.midmoney.feature.watchlist.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyEmptyState
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScreenHeader
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.feature.watchlist.R

@Composable
fun WatchlistRoute(viewModel: WatchlistViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WatchlistScreen(uiState = uiState)
}

@Composable
private fun WatchlistScreen(uiState: WatchlistUiState) {
    MidMoneyScaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            MidMoneyScreenHeader(title = stringResource(R.string.watchlist_title))
            MidMoneyEmptyState(
                icon = Icons.Outlined.Star,
                title = stringResource(R.string.watchlist_empty_title),
                description = stringResource(R.string.watchlist_empty_description),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun WatchlistScreenPreview() {
    MidMoneyTheme {
        WatchlistScreen(uiState = WatchlistUiState())
    }
}
