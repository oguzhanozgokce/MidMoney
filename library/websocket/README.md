# :library:websocket

Generic, provider-agnostic WebSocket transport over OkHttp.

## Contents

| Type | Purpose |
|---|---|
| `WebSocketClient` | `connect(url): Flow<WebSocketEvent>` — opens a connection, streams events until cancelled. |
| `WebSocketEvent` | `Connected(sender)` / `MessageReceived` / `Disconnected` / `Failure`. |
| `WebSocketSender` | Sends text frames (e.g. subscriptions); delivered with the `Connected` event. |

## Notes
- Reuses the app's shared `OkHttpClient` (injected), so any auth interceptor and logging apply, and
  adds a ping interval to keep idle connections alive.
- Knows nothing about Finnhub: provider-specific framing (subscribe messages, payload parsing) lives
  in the consumer's data layer (e.g. `:plugin:market`).
