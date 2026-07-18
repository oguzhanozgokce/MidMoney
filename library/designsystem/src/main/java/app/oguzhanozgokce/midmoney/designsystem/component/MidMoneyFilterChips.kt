package app.oguzhanozgokce.midmoney.designsystem.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme

/**
 * A horizontally scrollable row of single-choice filter chips styled as filled pills (primary when
 * selected). Generic over string labels so any screen can reuse it.
 */
@Composable
fun MidMoneyFilterChips(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    optionTestTags: List<String> = emptyList(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEachIndexed { index, label ->
            FilterChip(
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
                label = { Text(text = label) },
                shape = CircleShape,
                border = null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = optionTestTags.getOrNull(index)?.let { Modifier.testTag(it) } ?: Modifier,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MidMoneyFilterChipsPreview() {
    MidMoneyTheme {
        MidMoneyFilterChips(
            options = listOf("Popular", "Gainers", "Losers"),
            selectedIndex = 0,
            onSelect = {},
        )
    }
}
