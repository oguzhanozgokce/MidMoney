package app.oguzhanozgokce.midmoney.event

interface AnalyticsTracker {
    fun track(event: AnalyticsEvent)
    fun setUserId(id: String?)
}
