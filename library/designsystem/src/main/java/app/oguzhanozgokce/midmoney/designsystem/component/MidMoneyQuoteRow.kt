package app.oguzhanozgokce.midmoney.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import kotlin.math.abs

/**
 * A market list row: a colored symbol monogram, the symbol and name, and the price with a
 * color-coded change. Domain-agnostic — callers pass already-formatted strings.
 */
@Composable
fun MidMoneyQuoteRow(
    symbol: String,
    name: String,
    priceText: String,
    changeText: String,
    isPositive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SymbolMonogram(symbol = symbol)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = priceText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = changeText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (isPositive) {
                    MidMoneyTheme.extraColors.priceUp
                } else {
                    MidMoneyTheme.extraColors.priceDown
                },
            )
        }
    }
}

private val monogramColors = listOf(
    Color(0xFF4959EA),
    Color(0xFF00897B),
    Color(0xFFD81B60),
    Color(0xFF6D4C41),
    Color(0xFF3949AB),
    Color(0xFF00838F),
    Color(0xFFEF6C00),
    Color(0xFF5E35B1),
)

@Composable
private fun SymbolMonogram(symbol: String) {
    val background = monogramColors[abs(symbol.hashCode()) % monogramColors.size]
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol.take(1),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@PreviewLightDark
@Composable
private fun MidMoneyQuoteRowPreview() {
    MidMoneyTheme {
        MidMoneyQuoteRow(
            symbol = "AAPL",
            name = "Apple Inc.",
            priceText = "150.25",
            changeText = "+1.20%",
            isPositive = true,
            onClick = {},
        )
    }
}
