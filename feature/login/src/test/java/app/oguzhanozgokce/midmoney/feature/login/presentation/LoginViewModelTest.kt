package app.oguzhanozgokce.midmoney.feature.login.presentation

import app.cash.turbine.test
import app.oguzhanozgokce.midmoney.feature.login.analytics.LoginAnalyticsEvent
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.NavigationCommand
import app.oguzhanozgokce.midmoney.plugin.user.UserClient
import app.oguzhanozgokce.midmoney.plugin.user.domain.model.AuthError
import app.oguzhanozgokce.midmoney.plugin.user.domain.model.AuthException
import app.oguzhanozgokce.midmoney.plugin.user.domain.repository.AuthRepository
import app.oguzhanozgokce.midmoney.testing.FakeAnalytics
import app.oguzhanozgokce.midmoney.testing.FakeNavigator
import app.oguzhanozgokce.midmoney.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val navigator = FakeNavigator()
    private val analytics = FakeAnalytics()

    private fun viewModel(authRepository: AuthRepository) = LoginViewModel(
        userClient = UserClient(authRepository),
        navigator = navigator,
        analytics = analytics,
    )

    @Test
    fun `email and password changes are reflected in the state`() = runTest {
        val viewModel = viewModel(FakeAuthRepository())

        viewModel.onAction(LoginUiAction.EmailChanged("user@midmoney.app"))
        viewModel.onAction(LoginUiAction.PasswordChanged("secret"))

        val state = viewModel.currentUiState
        assertThat(state.email).isEqualTo("user@midmoney.app")
        assertThat(state.password).isEqualTo("secret")
    }

    @Test
    fun `blank fields show a message and never reach the auth repository`() = runTest {
        val repository = FakeAuthRepository()
        val viewModel = viewModel(repository)

        viewModel.uiEffect.test {
            viewModel.onAction(LoginUiAction.LoginClicked)
            assertThat(awaitItem()).isInstanceOf(LoginUiEffect.ShowMessage::class.java)
        }

        assertThat(repository.loginAttempts).isEmpty()
        assertThat(analytics.trackedEvents).doesNotContain(LoginAnalyticsEvent.LoginClicked)
        assertThat(viewModel.currentUiState.isLoading).isFalse()
    }

    @Test
    fun `successful login clears the back stack to home and tracks success`() = runTest {
        val viewModel = viewModel(FakeAuthRepository())
        viewModel.onAction(LoginUiAction.EmailChanged("user@midmoney.app"))
        viewModel.onAction(LoginUiAction.PasswordChanged("secret"))

        viewModel.onAction(LoginUiAction.LoginClicked)

        assertThat(navigator.commandsLog)
            .contains(NavigationCommand.NavigateAndClearBackStack(Destination.Home))
        assertThat(analytics.trackedEvents).containsAtLeast(
            LoginAnalyticsEvent.LoginClicked,
            LoginAnalyticsEvent.LoginSucceeded,
        )
    }

    @Test
    fun `failed login stops loading, shows a message and tracks the reason`() = runTest {
        val viewModel = viewModel(
            FakeAuthRepository(error = AuthException(AuthError.InvalidCredentials)),
        )
        viewModel.onAction(LoginUiAction.EmailChanged("user@midmoney.app"))
        viewModel.onAction(LoginUiAction.PasswordChanged("wrong"))

        viewModel.uiEffect.test {
            viewModel.onAction(LoginUiAction.LoginClicked)
            assertThat(awaitItem()).isInstanceOf(LoginUiEffect.ShowMessage::class.java)
        }

        assertThat(viewModel.currentUiState.isLoading).isFalse()
        assertThat(navigator.commandsLog).isEmpty()
        assertThat(analytics.trackedEvents)
            .contains(LoginAnalyticsEvent.LoginFailed(AuthError.InvalidCredentials.name))
    }

    @Test
    fun `an unknown throwable is reported as the unknown auth reason`() = runTest {
        val viewModel = viewModel(FakeAuthRepository(error = RuntimeException("boom")))
        viewModel.onAction(LoginUiAction.EmailChanged("user@midmoney.app"))
        viewModel.onAction(LoginUiAction.PasswordChanged("secret"))

        viewModel.uiEffect.test {
            viewModel.onAction(LoginUiAction.LoginClicked)
            awaitItem()
        }

        assertThat(analytics.trackedEvents)
            .contains(LoginAnalyticsEvent.LoginFailed(AuthError.Unknown.name))
    }
}

private class FakeAuthRepository(
    private val error: Throwable? = null,
) : AuthRepository {
    val loginAttempts: MutableList<String> = mutableListOf()

    override val currentUserId: Flow<String?> = flowOf(null)
    override val currentUserEmail: Flow<String?> = flowOf(null)

    override fun isCurrentlyLoggedIn(): Boolean = false

    override suspend fun login(email: String, password: String): Result<Unit> {
        loginAttempts += email
        return error?.let { Result.failure(it) } ?: Result.success(Unit)
    }

    override suspend fun register(email: String, password: String): Result<Unit> =
        error?.let { Result.failure(it) } ?: Result.success(Unit)

    override fun logout() = Unit
}
