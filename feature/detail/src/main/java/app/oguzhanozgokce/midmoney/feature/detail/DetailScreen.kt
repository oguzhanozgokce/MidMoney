package app.oguzhanozgokce.midmoney.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.Quote

@Composable
fun DetailRoute(
    symbol: String,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(symbol) {
        viewModel.onAction(DetailUiAction.Load(symbol))
    }

    DetailScreen(uiState = uiState, onAction = viewModel::onAction)
}

@Composable
private fun DetailScreen(
    uiState: DetailUiState,
    onAction: (DetailUiAction) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = uiState.symbol, style = MaterialTheme.typography.headlineLarge)

            uiState.livePrice?.let { price ->
                Text(
                    text = "Live: %.2f".format(price),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            when {
                uiState.isLoading -> Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }

                uiState.errorMessage != null -> Text(
                    text = uiState.errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                )

                uiState.quote != null -> QuoteDetails(uiState.quote)
            }

            Button(
                onClick = { onAction(DetailUiAction.BackClicked) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Back")
            }
        }
    }
}

@Composable
private fun QuoteDetails(quote: Quote) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DetailRow(label = "Current", value = quote.current)
        DetailRow(label = "Change", value = quote.change)
        DetailRow(label = "Change %", value = quote.percentChange)
        HorizontalDivider()
        DetailRow(label = "Open", value = quote.open)
        DetailRow(label = "High", value = quote.high)
        DetailRow(label = "Low", value = quote.low)
        DetailRow(label = "Previous close", value = quote.previousClose)
    }
}

@Composable
private fun DetailRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = "%.2f".format(value), style = MaterialTheme.typography.bodyLarge)
    }
}
