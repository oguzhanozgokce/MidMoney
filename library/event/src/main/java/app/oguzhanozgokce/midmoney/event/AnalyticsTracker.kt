package app.oguzhanozgokce.midmoney.event

interface AnalyticsTracker {
    val supplier: EventSupplier
    fun track(event: AnalyticsEvent)
    fun setUserId(id: String?)
}
