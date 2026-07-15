package app.oguzhanozgokce.midmoney.feature.market

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import app.oguzhanozgokce.midmoney.navigation.Destination
import app.oguzhanozgokce.midmoney.navigation.EntryProviderInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.marketEntry() {
    entry<Destination.Market> { MarketRoute() }
}

@Module
@InstallIn(SingletonComponent::class)
object MarketNavigationModule {

    @Provides
    @IntoSet
    fun provideMarketEntry(): EntryProviderInstaller = { marketEntry() }
}
