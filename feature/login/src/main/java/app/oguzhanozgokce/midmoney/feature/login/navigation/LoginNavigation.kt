package app.oguzhanozgokce.midmoney.feature.login.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import app.oguzhanozgokce.midmoney.feature.login.presentation.LoginRoute
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.EntryProviderInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.loginEntry() {
    entry<Destination.Login> { LoginRoute() }
}

@Module
@InstallIn(SingletonComponent::class)
object LoginNavigationModule {

    @Provides
    @IntoSet
    fun provideLoginEntry(): EntryProviderInstaller = { loginEntry() }
}
