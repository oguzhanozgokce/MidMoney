package app.oguzhanozgokce.midmoney.feature.detail.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButton
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyButtonSize
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyEmptyState
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyLoading
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.feature.detail.presentation.component.NewsSection
import app.oguzhanozgokce.midmoney.feature.detail.presentation.model.QuoteDetailUi

private val COLLAPSE_THRESHOLD = 130.dp

@Composable
fun DetailRoute(
    symbol: String,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(symbol) {
        viewModel.onAction(DetailUiAction.Load(symbol))
    }
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is DetailUiEffect.ShowMessage ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    DetailScreen(uiState = uiState, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreen(
    uiState: DetailUiState,
    onAction: (DetailUiAction) -> Unit,
) {
    val scrollState = rememberScrollState()
    val thresholdPx = with(LocalDensity.current) { COLLAPSE_THRESHOLD.toPx() }
    val titleVisible by remember { derivedStateOf { scrollState.value > thresholdPx } }

    MidMoneyScaffold(
        topBar = { DetailTopBar(uiState = uiState, titleVisible = titleVisible, onAction = onAction) },
        bottomBar = { if (uiState.quote != null) DetailActions(onAction = onAction) },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                uiState.isLoading -> MidMoneyLoading()

                uiState.errorMessage != null -> MidMoneyEmptyState(
                    icon = Icons.Outlined.CloudOff,
                    title = "Something went wrong",
                    description = "We couldn't load this stock. Please try again.\n\n${uiState.errorMessage}",
                    actionText = "Retry",
                    onActionClick = { onAction(DetailUiAction.Retry) },
                )

                uiState.quote != null -> DetailContent(uiState = uiState, scrollState = scrollState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTopBar(
    uiState: DetailUiState,
    titleVisible: Boolean,
    onAction: (DetailUiAction) -> Unit,
) {
    TopAppBar(
        title = {
            AnimatedVisibility(visible = titleVisible, enter = fadeIn(), exit = fadeOut()) {
                Column {
                    Text(
                        text = uiState.symbol,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    val price = uiState.livePriceText ?: uiState.quote?.current
                    if (price != null) {
                        Text(
                            text = price,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { onAction(DetailUiAction.BackClicked) }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { onAction(DetailUiAction.ToggleSave) }) {
                Icon(
                    imageVector = if (uiState.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Composable
private fun DetailContent(
    uiState: DetailUiState,
    scrollState: androidx.compose.foundation.ScrollState,
) {
    val quote = uiState.quote ?: return
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        Header(uiState = uiState, quote = quote)
        Statistics(quote = quote)
        NewsSection(
            news = uiState.news,
            isLoading = uiState.isNewsLoading,
            onOpen = { url -> uriHandler.openUri(url) },
        )
    }
}

@Composable
private fun Header(uiState: DetailUiState, quote: QuoteDetailUi) {
    val changeColor = if (quote.isPositive) {
        MidMoneyTheme.extraColors.priceUp
    } else {
        MidMoneyTheme.extraColors.priceDown
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = uiState.symbol,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = uiState.name.ifBlank { uiState.symbol },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = uiState.livePriceText ?: quote.current,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = quote.change,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = changeColor,
            )
            Text(
                text = quote.changePercent,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = changeColor,
            )
        }
    }
}

@Composable
private fun Statistics(quote: QuoteDetailUi) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCell(label = "Open", value = quote.open, modifier = Modifier.weight(1f))
            StatCell(label = "Previous close", value = quote.previousClose, modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCell(label = "High", value = quote.high, modifier = Modifier.weight(1f))
            StatCell(label = "Low", value = quote.low, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCell(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

@Composable
private fun DetailActions(onAction: (DetailUiAction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MidMoneyButton(
            text = "Sell",
            onClick = { onAction(DetailUiAction.SellClicked) },
            size = MidMoneyButtonSize.Large,
            modifier = Modifier.weight(1f),
        )
        MidMoneyButton(
            text = "Buy",
            onClick = { onAction(DetailUiAction.BuyClicked) },
            size = MidMoneyButtonSize.Large,
            modifier = Modifier.weight(1f),
        )
    }
}

@PreviewLightDark
@Composable
private fun DetailScreenPreview(
    @PreviewParameter(DetailUiStatePreviewProvider::class) state: DetailUiState,
) {
    MidMoneyTheme {
        DetailScreen(uiState = state, onAction = {})
    }
}
