package app.oguzhanozgokce.midmoney.websocket

sealed interface WebSocketEvent {
    data class Connected(val sender: WebSocketSender) : WebSocketEvent
    data class MessageReceived(val text: String) : WebSocketEvent
    data class Disconnected(val code: Int, val reason: String) : WebSocketEvent
    data class Failure(val throwable: Throwable) : WebSocketEvent
}

fun interface WebSocketSender {
    fun send(text: String): Boolean
}
