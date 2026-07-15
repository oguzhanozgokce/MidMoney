package app.oguzhanozgokce.midmoney.feature.market

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MarketRoute(viewModel: MarketViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MarketScreen(uiState = uiState, onAction = viewModel::onAction)
}

@Composable
private fun MarketScreen(
    uiState: MarketUiState,
    onAction: (MarketUiAction) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Market", style = MaterialTheme.typography.headlineLarge)
            Button(
                onClick = { onAction(MarketUiAction.OpenDetail(symbol = "AAPL")) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Open AAPL detail")
            }
            Button(
                onClick = { onAction(MarketUiAction.OpenWatchlist) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Open watchlist")
            }
        }
    }
}
