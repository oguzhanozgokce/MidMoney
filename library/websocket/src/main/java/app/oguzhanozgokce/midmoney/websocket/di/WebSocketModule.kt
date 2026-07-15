package app.oguzhanozgokce.midmoney.websocket.di

import app.oguzhanozgokce.midmoney.websocket.OkHttpWebSocketClient
import app.oguzhanozgokce.midmoney.websocket.WebSocketClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebSocketModule {

    @Binds
    @Singleton
    abstract fun bindWebSocketClient(impl: OkHttpWebSocketClient): WebSocketClient
}
