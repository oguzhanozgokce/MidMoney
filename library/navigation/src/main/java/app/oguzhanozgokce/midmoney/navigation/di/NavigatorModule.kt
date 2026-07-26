package app.oguzhanozgokce.midmoney.navigation.di

import app.oguzhanozgokce.midmoney.navigation.DefaultNavigator
import app.oguzhanozgokce.midmoney.navigation.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavigatorModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(impl: DefaultNavigator): Navigator
}
