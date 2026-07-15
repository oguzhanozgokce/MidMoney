package app.oguzhanozgokce.midmoney.common.extensions

import java.util.Locale

/**
 * Display formatting for numbers. Lives here (not in the UI) so composables render ready-made
 * strings and the formatting stays testable and consistent.
 */
fun Double.formatPrice(): String = String.format(Locale.US, "%.2f", this)

fun Double.formatSignedPercent(): String = String.format(Locale.US, "%+.2f%%", this)
