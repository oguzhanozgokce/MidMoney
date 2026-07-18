package app.oguzhanozgokce.midmoney.event

import javax.inject.Inject

class CompositeAnalytics @Inject constructor(
    private val trackers: Set<@JvmSuppressWildcards AnalyticsTracker>,
) : Analytics {

    override fun track(event: AnalyticsEvent) {
        trackers.forEach { it.track(event) }
    }

    override fun setUserId(id: String?) {
        trackers.forEach { it.setUserId(id) }
    }
}
