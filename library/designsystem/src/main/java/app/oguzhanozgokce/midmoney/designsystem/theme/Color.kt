package app.oguzhanozgokce.midmoney.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Brand — green conveys growth/money.
val Green40 = Color(0xFF006D3B)
val Green80 = Color(0xFF52DB92)
val GreenGrey40 = Color(0xFF4F6354)
val GreenGrey80 = Color(0xFFB6CCB9)

// Light neutrals
val LightBackground = Color(0xFFFBFDF8)
val LightOnBackground = Color(0xFF191C19)
val LightSurfaceVariant = Color(0xFFDCE5DB)
val LightOnSurfaceVariant = Color(0xFF414942)

// Dark neutrals
val DarkBackground = Color(0xFF191C19)
val DarkOnBackground = Color(0xFFE1E3DD)
val DarkSurfaceVariant = Color(0xFF414942)
val DarkOnSurfaceVariant = Color(0xFFC0C9BF)

// Semantic price colors — Material's ColorScheme has no slot for these, so they live in ExtendedColors.
val PriceUpLight = Color(0xFF1B8E3D)
val PriceUpDark = Color(0xFF5CD98A)
val PriceDownLight = Color(0xFFD32F2F)
val PriceDownDark = Color(0xFFF28B82)

/**
 * App-specific semantic colors that Material's [androidx.compose.material3.ColorScheme] does not
 * cover (e.g. price direction). Provided by `MidMoneyTheme` and read via `MidMoneyTheme.extendedColors`.
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
