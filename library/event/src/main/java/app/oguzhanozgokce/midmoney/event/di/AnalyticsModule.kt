package app.oguzhanozgokce.midmoney.event.di

import android.content.Context
import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.event.FirebaseAnalyticsClient
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context,
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsBindsModule {

    @Binds
    @Singleton
    abstract fun bindAnalytics(impl: FirebaseAnalyticsClient): Analytics
}
