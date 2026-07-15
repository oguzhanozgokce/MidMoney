package app.oguzhanozgokce.midmoney.common.extensions

fun Int?.orZero(): Int = this ?: 0
fun Long?.orZero(): Long = this ?: 0L
fun Double?.orZero(): Double = this ?: 0.0
fun Float?.orZero(): Float = this ?: 0f
fun Boolean?.orFalse(): Boolean = this ?: false
fun Boolean?.orTrue(): Boolean = this ?: true
