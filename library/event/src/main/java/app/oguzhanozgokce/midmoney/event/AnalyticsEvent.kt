package app.oguzhanozgokce.midmoney.event

/**
 * A trackable event. Each feature defines its own events by implementing this interface, so event
 * definitions stay next to the code that fires them while the tracker stays central.
 */
interface AnalyticsEvent {
    val name: String
    val params: Map<String, String> get() = emptyMap()
}
