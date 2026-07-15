package app.oguzhanozgokce.midmoney.feature.detail.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import app.oguzhanozgokce.midmoney.feature.detail.presentation.DetailRoute
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.EntryProviderInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.detailEntry() {
    entry<Destination.Detail> { key -> DetailRoute(symbol = key.symbol) }
}

@Module
@InstallIn(SingletonComponent::class)
object DetailNavigationModule {

    @Provides
    @IntoSet
    fun provideDetailEntry(): EntryProviderInstaller = { detailEntry() }
}
