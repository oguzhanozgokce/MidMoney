package app.oguzhanozgokce.midmoney

import app.oguzhanozgokce.midmoney.navigation.EntryProviderInstaller
import app.oguzhanozgokce.midmoney.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object AppNavigationModule {

    // TEMPORARY: placeholder entries until the :feature modules contribute their own (Phase 6).
    @Provides
    @IntoSet
    fun providePlaceholderEntries(navigator: Navigator): EntryProviderInstaller = {
        placeholderEntries(navigator)
    }
}
