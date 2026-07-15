package app.oguzhanozgokce.midmoney.feature.watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButton
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold

@Composable
fun WatchlistRoute(viewModel: WatchlistViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WatchlistScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
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
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Watchlist", style = MaterialTheme.typography.headlineLarge)
            MidMoneyButton(
                text = "Back",
                onClick = { onAction(WatchlistUiAction.BackClicked) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
