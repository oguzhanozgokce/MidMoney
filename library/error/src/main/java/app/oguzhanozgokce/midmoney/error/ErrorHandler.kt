package app.oguzhanozgokce.midmoney.error

import app.oguzhanozgokce.midmoney.common.coroutines.DispatcherProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class ErrorHandler @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val mappers: Set<@JvmSuppressWildcards ErrorMapper>,
) {

    @Suppress("TooGenericExceptionCaught")
    suspend fun <T> call(
        context: CoroutineContext = dispatchers.io,
        transform: (Throwable) -> Throwable = { it.toAppException() },
        block: suspend () -> T,
    ): Result<T> = withContext(context) {
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.failure(transform(e))
        }
    }

    fun <T> Flow<T>.asResult(): Flow<Result<T>> =
        map { Result.success(it) }.catch { emit(Result.failure(it.toAppException())) }

    fun Throwable.toAppException(): AppException =
        this as? AppException ?: AppException(toAppError(this), this)

    fun toAppError(throwable: Throwable): AppError = (throwable as? AppException)?.error
        ?: mappers.firstNotNullOfOrNull { it.map(throwable) }
        ?: AppError.Unknown
}
