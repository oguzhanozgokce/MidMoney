package app.oguzhanozgokce.midmoney.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.oguzhanozgokce.midmoney.designsystem.theme.DisplayFontFamily
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme

/**
 * The standard top title used by the bottom-navigation tabs. A plain header (not a Material top app
 * bar) so every tab shares the same wordmark styling and spacing, keeping tab switches seamless.
 */
@Composable
fun MidMoneyScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = DisplayFontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@PreviewLightDark
@Composable
private fun MidMoneyScreenHeaderPreview() {
    MidMoneyTheme {
        MidMoneyScreenHeader(title = "Watchlist")
    }
}
