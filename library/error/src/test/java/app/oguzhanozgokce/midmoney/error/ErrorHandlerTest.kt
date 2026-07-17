package app.oguzhanozgokce.midmoney.error

import app.oguzhanozgokce.midmoney.common.coroutines.DispatcherProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ErrorHandlerTest {

    private val dispatchers = TestDispatcherProvider(UnconfinedTestDispatcher())

    private fun handler(vararg mappers: ErrorMapper) = ErrorHandler(dispatchers, mappers.toSet())

    @Test
    fun `call returns success for a value`() = runTest {
        val result = handler().call { 42 }

        assertThat(result.getOrNull()).isEqualTo(42)
    }

    @Test
    fun `call maps an unrecognized throwable to Unknown`() = runTest {
        val result = handler().call { error("boom") }

        assertThat(result.appError()).isEqualTo(AppError.Unknown)
    }

    @Test
    fun `call uses the first mapper that recognizes the throwable`() = runTest {
        val result = handler(
            ErrorMapper { null },
            ErrorMapper { AppError.Server(500) },
        ).call { throw IllegalStateException() }

        assertThat(result.appError()).isEqualTo(AppError.Server(500))
    }

    @Test
    fun `call keeps an existing AppException error`() = runTest {
        val result = handler().call { throw AppException(AppError.Network) }

        assertThat(result.appError()).isEqualTo(AppError.Network)
    }

    private fun Result<*>.appError(): AppError? = (exceptionOrNull() as? AppException)?.error

    @Test
    fun `call rethrows cancellation`() = runTest {
        val thrown = runCatching {
            handler().call { throw CancellationException() }
        }.exceptionOrNull()

        assertThat(thrown).isInstanceOf(CancellationException::class.java)
    }
}

private class TestDispatcherProvider(dispatcher: CoroutineDispatcher) : DispatcherProvider {
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val main: CoroutineDispatcher = dispatcher
}
