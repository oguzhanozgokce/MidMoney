package app.oguzhanozgokce.midmoney.plugin.market.di

import app.oguzhanozgokce.midmoney.plugin.market.data.remote.FinnhubApi
import app.oguzhanozgokce.midmoney.plugin.market.data.repository.MarketRepositoryImpl
import app.oguzhanozgokce.midmoney.plugin.market.domain.repository.MarketRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MarketDataModule {

    @Provides
    @Singleton
    fun provideFinnhubApi(retrofit: Retrofit): FinnhubApi =
        retrofit.create(FinnhubApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MarketRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMarketRepository(impl: MarketRepositoryImpl): MarketRepository
}
