package app.oguzhanozgokce.midmoney.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Small rounded pill for compact status text (e.g. a price-change percentage). */
@Composable
fun MidMoneyBadge(
    text: String,
    contentColor: Color,
    modifier: Modifier = Modifier,
    containerColor: Color = contentColor.copy(alpha = 0.15f),
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = contentColor,
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}
