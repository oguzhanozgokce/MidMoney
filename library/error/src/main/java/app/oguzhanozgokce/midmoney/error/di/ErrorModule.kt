package app.oguzhanozgokce.midmoney.error.di

import app.oguzhanozgokce.midmoney.error.ErrorMapper
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds

@Module
@InstallIn(SingletonComponent::class)
interface ErrorModule {

    @Multibinds
    fun errorMappers(): Set<ErrorMapper>
}
