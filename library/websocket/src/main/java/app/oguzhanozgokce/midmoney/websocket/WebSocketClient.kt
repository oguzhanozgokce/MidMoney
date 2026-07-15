package app.oguzhanozgokce.midmoney.websocket

import kotlinx.coroutines.flow.Flow

/**
 * Generic WebSocket transport. [connect] opens a connection to [url] and emits [WebSocketEvent]s
 * until the flow is cancelled or the connection closes. Not tied to any provider.
 */
interface WebSocketClient {
    fun connect(url: String): Flow<WebSocketEvent>
}
