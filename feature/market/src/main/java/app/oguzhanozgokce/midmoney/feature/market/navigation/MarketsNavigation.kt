package app.oguzhanozgokce.midmoney.feature.market.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import app.oguzhanozgokce.midmoney.feature.market.presentation.MarketsRoute
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.EntryProviderInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.marketsEntry() {
    entry<Destination.Markets> { MarketsRoute() }
}

@Module
@InstallIn(SingletonComponent::class)
object MarketsNavigationModule {

    @Provides
    @IntoSet
    fun provideMarketsEntry(): EntryProviderInstaller = { marketsEntry() }
}
