package app.oguzhanozgokce.midmoney.event

interface Analytics {
    fun track(event: AnalyticsEvent)
    fun setUserId(id: String?)
}
