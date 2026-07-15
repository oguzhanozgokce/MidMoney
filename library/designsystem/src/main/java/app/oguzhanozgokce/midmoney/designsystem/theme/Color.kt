package app.oguzhanozgokce.midmoney.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Green40 = Color(0xFF006D3B)
val Green80 = Color(0xFF52DB92)
val GreenGrey40 = Color(0xFF4F6354)
val GreenGrey80 = Color(0xFFB6CCB9)

val LightBackground = Color(0xFFFBFDF8)
val LightOnBackground = Color(0xFF191C19)
val LightSurfaceVariant = Color(0xFFDCE5DB)
val LightOnSurfaceVariant = Color(0xFF414942)

val DarkBackground = Color(0xFF191C19)
val DarkOnBackground = Color(0xFFE1E3DD)
val DarkSurfaceVariant = Color(0xFF414942)
val DarkOnSurfaceVariant = Color(0xFFC0C9BF)

val PriceUpLight = Color(0xFF1B8E3D)
val PriceUpDark = Color(0xFF5CD98A)
val PriceDownLight = Color(0xFFD32F2F)
val PriceDownDark = Color(0xFFF28B82)

val LightColorScheme = lightColorScheme(
    primary = Green40,
    secondary = GreenGrey40,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightBackground,
    onSurface = LightOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
)

val DarkColorScheme = darkColorScheme(
    primary = Green80,
    secondary = GreenGrey80,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkBackground,
    onSurface = DarkOnBackground,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
)

@Immutable
data class ExtraColors(
    val priceUp: Color,
    val priceDown: Color,
)

val LightExtraColors = ExtraColors(
    priceUp = PriceUpLight,
    priceDown = PriceDownLight,
)

val DarkExtraColors = ExtraColors(
    priceUp = PriceUpDark,
    priceDown = PriceDownDark,
)

val LocalExtraColors = staticCompositionLocalOf {
    ExtraColors(priceUp = Color.Unspecified, priceDown = Color.Unspecified)
}
