package app.oguzhanozgokce.midmoney.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Brand — Midas-style blue.
val Blue40 = Color(0xFF4959EA)
val OnBlueLight = Color(0xFFFFFFFF)
val BlueGrey40 = Color(0xFF565E71)
val BlueGrey80 = Color(0xFFBEC6DC)

val LightBackground = Color(0xFFFDFBFF)
val LightOnBackground = Color(0xFF1A1B20)
val LightSurfaceVariant = Color(0xFFE2E1EC)
val LightOnSurfaceVariant = Color(0xFF45464F)

val DarkBackground = Color(0xFF030304)
val DarkOnBackground = Color(0xFFE3E1E9)
val DarkSurface = Color(0xFF0C0D10)
val DarkSurfaceVariant = Color(0xFF17181D)
val DarkOnSurfaceVariant = Color(0xFF9DA0AC)

val PriceUpLight = Color(0xFF1B8E3D)
val PriceUpDark = Color(0xFF5CD98A)
val PriceDownLight = Color(0xFFD32F2F)
val PriceDownDark = Color(0xFFF28B82)

val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = OnBlueLight,
    secondary = BlueGrey40,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightBackground,
    onSurface = LightOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
)

val DarkColorScheme = darkColorScheme(
    primary = Blue40,
    onPrimary = OnBlueLight,
    secondary = BlueGrey80,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
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
