package app.oguzhanozgokce.midmoney.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme

@PreviewLightDark
@Composable
private fun MidMoneyButtonPreview() {
    MidMoneyTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MidMoneyButton(text = "Filled medium", onClick = {}, modifier = Modifier.fillMaxWidth())
                MidMoneyButton(
                    text = "Outlined",
                    onClick = {},
                    style = MidMoneyButtonStyle.Outlined,
                    modifier = Modifier.fillMaxWidth(),
                )
                MidMoneyButton(text = "Small", onClick = {}, size = MidMoneyButtonSize.Small)
                MidMoneyButton(text = "Large", onClick = {}, size = MidMoneyButtonSize.Large)
                MidMoneyButton(
                    text = "Loading",
                    onClick = {},
                    loading = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
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

@PreviewLightDark
@Composable
private fun MidMoneyLoadingPreview() {
    MidMoneyTheme {
        Surface(modifier = Modifier.size(160.dp)) {
            MidMoneyLoading()
        }
    }
}
