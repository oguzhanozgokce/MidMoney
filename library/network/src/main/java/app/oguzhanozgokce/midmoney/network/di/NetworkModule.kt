package app.oguzhanozgokce.midmoney.network.di

import app.oguzhanozgokce.midmoney.common.config.AppConfig
import app.oguzhanozgokce.midmoney.logger.MidMoneyLogger
import app.oguzhanozgokce.midmoney.network.BuildConfig
import app.oguzhanozgokce.midmoney.network.interceptor.ApiKeyInterceptor
import app.oguzhanozgokce.midmoney.network.interceptor.NetworkLoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // DTOs declare every field nullable and let the mapper normalize, so an omitted or explicitly
    // null field decodes to null instead of failing the response — no value coercion needed here.
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(json: Json): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor(BuildConfig.FINNHUB_API_KEY))
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    NetworkLoggingInterceptor(json) { message ->
                        MidMoneyLogger.d(message, tag = "Network")
                    },
                )
            }
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(json: Json, client: OkHttpClient, appConfig: AppConfig): Retrofit = Retrofit.Builder()
        .baseUrl(appConfig.baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
