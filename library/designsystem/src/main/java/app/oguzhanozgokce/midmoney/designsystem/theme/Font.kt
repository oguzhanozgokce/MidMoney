package app.oguzhanozgokce.midmoney.designsystem.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import app.oguzhanozgokce.midmoney.designsystem.R

/**
 * Space Grotesk, bundled as a font resource so it renders offline and in Compose previews. Used for
 * the app wordmark.
 */
val DisplayFontFamily: FontFamily = FontFamily(
    Font(R.font.space_grotesk_regular, FontWeight.Normal),
    Font(R.font.space_grotesk_medium, FontWeight.Medium),
    Font(R.font.space_grotesk_bold, FontWeight.Bold),
)
