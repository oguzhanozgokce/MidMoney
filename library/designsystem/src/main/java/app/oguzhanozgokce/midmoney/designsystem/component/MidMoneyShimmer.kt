package app.oguzhanozgokce.midmoney.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

@Composable
fun MidMoneyShimmer(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1200, easing = LinearEasing)),
        label = "shimmerProgress",
    )
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    Box(
        modifier = modifier.drawWithCache {
            val width = size.width
            val start = -width + progress * 2f * width
            val brush = Brush.linearGradient(
                colors = listOf(base, highlight, base),
                start = Offset(start, 0f),
                end = Offset(start + width, 0f),
            )
            onDrawBehind { drawRect(brush) }
        },
    )
}
