package app.oguzhanozgokce.midmoney.common.extensions

import java.util.Locale

fun Double.formatPrice(): String = String.format(Locale.US, "%.2f", this)

fun Double.formatSignedPercent(): String = String.format(Locale.US, "%+.2f%%", this)
