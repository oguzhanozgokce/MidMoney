package app.oguzhanozgokce.midmoney.feature.profile.presentation

import app.cash.turbine.test
import app.oguzhanozgokce.midmoney.common.appinfo.AppInfoProvider
import app.oguzhanozgokce.midmoney.common.config.AppConfig
import app.oguzhanozgokce.midmoney.feature.profile.analytics.ProfileAnalyticsEvent
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.NavigationCommand
import app.oguzhanozgokce.midmoney.plugin.user.UserClient
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
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val navigator = FakeNavigator()
    private val analytics = FakeAnalytics()

    private fun viewModel(
        authRepository: AuthRepository = FakeAuthRepository(),
        appConfig: AppConfig = FakeAppConfig(),
    ) = ProfileViewModel(
        userClient = UserClient(authRepository),
        navigator = navigator,
        analytics = analytics,
        appInfo = FakeAppInfoProvider(),
        appConfig = appConfig,
    )

    @Test
    fun `exposes the app version and tracks the viewed event`() = runTest {
        val viewModel = viewModel()

        assertThat(viewModel.currentUiState.versionName).isEqualTo("1.0.0")
        assertThat(analytics.trackedEvents).contains(ProfileAnalyticsEvent.Viewed)
    }

    @Test
    fun `shows an uppercase environment badge on preprod`() = runTest {
        val viewModel = viewModel(appConfig = FakeAppConfig(environment = "preprod", isPreprod = true))

        assertThat(viewModel.currentUiState.environmentLabel).isEqualTo("PREPROD")
    }

    @Test
    fun `hides the environment badge on prod`() = runTest {
        val viewModel = viewModel(appConfig = FakeAppConfig(environment = "prod", isPreprod = false))

        assertThat(viewModel.currentUiState.environmentLabel).isNull()
    }

    @Test
    fun `observes the signed in email`() = runTest {
        val viewModel = viewModel(FakeAuthRepository(email = "user@midmoney.app"))

        assertThat(viewModel.currentUiState.email).isEqualTo("user@midmoney.app")
    }

    @Test
    fun `logout signs out, clears the back stack to login and tracks logout`() = runTest {
        val repository = FakeAuthRepository()
        val viewModel = viewModel(repository)

        viewModel.onAction(ProfileUiAction.Logout)

        assertThat(repository.loggedOut).isTrue()
        assertThat(navigator.commandsLog)
            .contains(NavigationCommand.NavigateAndClearBackStack(Destination.Login))
        assertThat(analytics.trackedEvents).contains(ProfileAnalyticsEvent.Logout)
    }

    @Test
    fun `a menu item shows the coming soon message and tracks which item`() = runTest {
        val viewModel = viewModel()

        viewModel.uiEffect.test {
            viewModel.onAction(ProfileUiAction.ComingSoonClicked("security"))
            assertThat(awaitItem()).isInstanceOf(ProfileUiEffect.ShowMessage::class.java)
        }

        assertThat(analytics.trackedEvents).contains(ProfileAnalyticsEvent.MenuClicked("security"))
    }
}

private class FakeAuthRepository(
    private val email: String? = null,
) : AuthRepository {
    var loggedOut: Boolean = false
        private set

    override val currentUserId: Flow<String?> = flowOf(null)
    override val currentUserEmail: Flow<String?> = flowOf(email)

    override fun isCurrentlyLoggedIn(): Boolean = true
    override suspend fun login(email: String, password: String): Result<Unit> = Result.success(Unit)
    override suspend fun register(email: String, password: String): Result<Unit> = Result.success(Unit)

    override fun logout() {
        loggedOut = true
    }
}

private class FakeAppInfoProvider : AppInfoProvider {
    override val versionName: String = "1.0.0"
}

private class FakeAppConfig(
    override val environment: String = "prod",
    override val isPreprod: Boolean = false,
) : AppConfig {
    override val baseUrl: String = "https://example.test/"
}
