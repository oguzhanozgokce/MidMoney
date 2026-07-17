package app.oguzhanozgokce.midmoney.common.appinfo

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppInfoModule {

    @Binds
    @Singleton
    abstract fun bindAppInfoProvider(impl: DefaultAppInfoProvider): AppInfoProvider
}
