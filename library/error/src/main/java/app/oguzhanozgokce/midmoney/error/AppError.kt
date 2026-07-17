package app.oguzhanozgokce.midmoney.error

sealed interface AppError {
    data object Network : AppError
    data object Timeout : AppError
    data class Server(val code: Int) : AppError
    data object Unauthorized : AppError
    data object Unknown : AppError
}

class AppException(val error: AppError, cause: Throwable? = null) : Exception(cause)
