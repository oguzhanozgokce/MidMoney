package app.oguzhanozgokce.midmoney.network.error

import app.oguzhanozgokce.midmoney.error.ErrorMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkErrorModule {

    @Binds
    @IntoSet
    abstract fun bindNetworkErrorMapper(impl: NetworkErrorMapper): ErrorMapper
}
