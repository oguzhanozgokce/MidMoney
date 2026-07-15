package app.oguzhanozgokce.midmoney.event

/**
 * Analytics facade. ViewModels depend on this instead of Firebase directly, so the provider stays
 * swappable and events are easy to fake in tests.
 */
interface Analytics {
    fun track(event: AnalyticsEvent)
    fun setUserId(id: String?)
}
