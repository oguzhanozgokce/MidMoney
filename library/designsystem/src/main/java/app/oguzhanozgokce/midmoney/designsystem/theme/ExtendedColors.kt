package app.oguzhanozgokce.midmoney.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * App-specific semantic colors that Material's `ColorScheme` does not cover (e.g. price direction).
 * Provided by [MidMoneyTheme] and read via `MidMoneyTheme.extendedColors`.
 */
@Immutable
data class ExtendedColors(
    val priceUp: Color,
    val priceDown: Color,
)

val LightExtendedColors = ExtendedColors(
    priceUp = PriceUpLight,
    priceDown = PriceDownLight,
)

val DarkExtendedColors = ExtendedColors(
    priceUp = PriceUpDark,
    priceDown = PriceDownDark,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(priceUp = Color.Unspecified, priceDown = Color.Unspecified)
}
