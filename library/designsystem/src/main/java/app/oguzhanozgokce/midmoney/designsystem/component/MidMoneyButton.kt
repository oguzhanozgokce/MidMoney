package app.oguzhanozgokce.midmoney.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class MidMoneyButtonStyle { Filled, Outlined }

enum class MidMoneyButtonSize { Small, Medium, Large }

@Composable
fun MidMoneyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: MidMoneyButtonStyle = MidMoneyButtonStyle.Filled,
    size: MidMoneyButtonSize = MidMoneyButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val (minHeight, horizontalPadding, textStyle) = when (size) {
        MidMoneyButtonSize.Small -> Triple(36.dp, 12.dp, MaterialTheme.typography.labelLarge)
        MidMoneyButtonSize.Medium -> Triple(48.dp, 20.dp, MaterialTheme.typography.titleSmall)
        MidMoneyButtonSize.Large -> Triple(56.dp, 28.dp, MaterialTheme.typography.titleMedium)
    }
    val contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 8.dp)

    val content: @Composable RowScope.() -> Unit = {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = LocalContentColor.current,
            )
        } else {
            Text(text = text, style = textStyle)
        }
    }

    when (style) {
        MidMoneyButtonStyle.Filled -> Button(
            onClick = onClick,
            enabled = enabled && !loading,
            contentPadding = contentPadding,
            modifier = modifier.heightIn(min = minHeight),
            content = content,
        )
        MidMoneyButtonStyle.Outlined -> OutlinedButton(
            onClick = onClick,
            enabled = enabled && !loading,
            contentPadding = contentPadding,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = modifier.heightIn(min = minHeight),
            content = content,
        )
    }
}
