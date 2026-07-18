package app.oguzhanozgokce.midmoney.event

interface Analytics {
    fun track(event: AnalyticsEvent, vararg suppliers: EventSupplier)
    fun setUserId(id: String?)
}
