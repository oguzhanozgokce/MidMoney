package app.oguzhanozgokce.midmoney.error

fun interface ErrorMapper {
    fun map(throwable: Throwable): AppError?
}
