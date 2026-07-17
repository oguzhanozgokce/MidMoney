package app.oguzhanozgokce.midmoney.di

import app.oguzhanozgokce.midmoney.BuildConfig
import app.oguzhanozgokce.midmoney.common.config.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig = object : AppConfig {
        override val baseUrl: String = BuildConfig.BASE_URL
        override val environment: String = BuildConfig.ENVIRONMENT
        override val isPreprod: Boolean = BuildConfig.IS_PREPROD
    }
}
