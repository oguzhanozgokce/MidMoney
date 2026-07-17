package app.oguzhanozgokce.midmoney.error

fun AppError.messageRes(): Int = when (this) {
    AppError.Network -> R.string.error_network
    AppError.Timeout -> R.string.error_timeout
    is AppError.Server -> R.string.error_server
    AppError.Unauthorized -> R.string.error_unauthorized
    AppError.Unknown -> R.string.error_generic
}

fun Throwable?.errorMessageRes(): Int = ((this as? AppException)?.error ?: AppError.Unknown).messageRes()
