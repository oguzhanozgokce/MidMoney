// File groups the badge with its small style enum; it is named after the MidMoneyBadge composable.
@file:Suppress("MatchingDeclarationName")

package app.oguzhanozgokce.midmoney.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme

enum class MidMoneyBadgeStyle { Filled, Outlined }

@Composable
fun MidMoneyBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    style: MidMoneyBadgeStyle = MidMoneyBadgeStyle.Filled,
) {
    val filled = style == MidMoneyBadgeStyle.Filled
    Surface(
        shape = RoundedCornerShape(percent = 50),
        color = if (filled) color.copy(alpha = 0.15f) else Color.Transparent,
        contentColor = color,
        border = if (filled) null else BorderStroke(1.dp, color),
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun MidMoneyBadgePreview() {
    MidMoneyTheme {
        Surface {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MidMoneyBadge(text = "+1.50%", color = MidMoneyTheme.extraColors.priceUp)
                MidMoneyBadge(
                    text = "-0.80%",
                    color = MidMoneyTheme.extraColors.priceDown,
                    style = MidMoneyBadgeStyle.Outlined,
                )
            }
        }
    }
}
