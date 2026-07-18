package app.oguzhanozgokce.midmoney.remoteconfig.di

import app.oguzhanozgokce.midmoney.remoteconfig.BuildConfig
import app.oguzhanozgokce.midmoney.remoteconfig.FirebaseRemoteConfigClient
import app.oguzhanozgokce.midmoney.remoteconfig.RemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DEBUG_FETCH_INTERVAL_SECONDS = 0L
private const val RELEASE_FETCH_INTERVAL_SECONDS = 3600L

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val interval = if (BuildConfig.DEBUG) DEBUG_FETCH_INTERVAL_SECONDS else RELEASE_FETCH_INTERVAL_SECONDS
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(interval)
            .build()
        return FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(settings)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigBindsModule {

    @Binds
    @Singleton
    abstract fun bindRemoteConfig(impl: FirebaseRemoteConfigClient): RemoteConfig
}
