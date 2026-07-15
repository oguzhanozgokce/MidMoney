package app.oguzhanozgokce.midmoney.event

interface AnalyticsEvent {
    val name: String
    val params: Map<String, String> get() = emptyMap()
}
