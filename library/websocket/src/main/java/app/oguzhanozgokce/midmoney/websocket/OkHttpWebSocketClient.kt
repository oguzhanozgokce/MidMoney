package app.oguzhanozgokce.midmoney.websocket

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.time.Duration
import javax.inject.Inject

private const val NORMAL_CLOSURE_STATUS = 1000

class OkHttpWebSocketClient @Inject constructor(
    okHttpClient: OkHttpClient,
) : WebSocketClient {

    private val client = okHttpClient.newBuilder()
        .pingInterval(Duration.ofSeconds(PING_INTERVAL_SECONDS))
        .build()

    override fun connect(url: String): Flow<WebSocketEvent> = callbackFlow {
        val request = Request.Builder().url(url).build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                trySend(WebSocketEvent.Connected(WebSocketSender { text -> webSocket.send(text) }))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(WebSocketEvent.MessageReceived(text))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                trySend(WebSocketEvent.Disconnected(code, reason))
                webSocket.close(NORMAL_CLOSURE_STATUS, null)
                close()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                trySend(WebSocketEvent.Failure(t))
                close(t)
            }
        }

        val webSocket = client.newWebSocket(request, listener)
        awaitClose { webSocket.close(NORMAL_CLOSURE_STATUS, null) }
    }

    private companion object {
        const val PING_INTERVAL_SECONDS = 20L
    }
}
