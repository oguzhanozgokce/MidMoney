package app.oguzhanozgokce.midmoney.plugin.market.data.remote

import app.oguzhanozgokce.midmoney.plugin.market.data.remote.dto.TradeMessageDto
import app.oguzhanozgokce.midmoney.plugin.market.domain.model.TradePrice
import app.oguzhanozgokce.midmoney.websocket.WebSocketClient
import app.oguzhanozgokce.midmoney.websocket.WebSocketEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Streams live trade prices from the Finnhub WebSocket. On connect it subscribes to the requested
 * symbols; incoming `trade` messages are parsed into [TradePrice]s. The auth token is added to the
 * handshake by the shared OkHttp interceptor, so only the bare URL is needed here.
 */
class FinnhubTradeStream @Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val json: Json,
) {
    fun observePrices(symbols: List<String>): Flow<TradePrice> =
        webSocketClient.connect(FINNHUB_WS_URL).transform { event ->
            when (event) {
                is WebSocketEvent.Connected ->
                    symbols.forEach { symbol -> event.sender.send(subscribeMessage(symbol)) }
                is WebSocketEvent.MessageReceived -> emitTrades(event.text)
                is WebSocketEvent.Disconnected -> Unit
                is WebSocketEvent.Failure -> Unit
            }
        }

    private suspend fun FlowCollector<TradePrice>.emitTrades(text: String) {
        val message = runCatching { json.decodeFromString<TradeMessageDto>(text) }.getOrNull()
        if (message?.type != TRADE_TYPE) return
        message.data.orEmpty().forEach { dto ->
            val symbol = dto.symbol ?: return@forEach
            val price = dto.price ?: return@forEach
            emit(TradePrice(symbol = symbol, price = price))
        }
    }

    private fun subscribeMessage(symbol: String): String =
        """{"type":"subscribe","symbol":"$symbol"}"""

    private companion object {
        const val FINNHUB_WS_URL = "wss://ws.finnhub.io"
        const val TRADE_TYPE = "trade"
    }
}
