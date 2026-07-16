package app.oguzhanozgokce.midmoney.plugin.news.di

import app.oguzhanozgokce.midmoney.plugin.news.data.remote.NewsApi
import app.oguzhanozgokce.midmoney.plugin.news.data.repository.NewsRepositoryImpl
import app.oguzhanozgokce.midmoney.plugin.news.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsDataModule {

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NewsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository
}
