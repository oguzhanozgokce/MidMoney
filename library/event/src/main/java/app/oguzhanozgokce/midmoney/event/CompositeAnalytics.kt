package app.oguzhanozgokce.midmoney.event

import javax.inject.Inject

class CompositeAnalytics @Inject constructor(
    trackers: Set<@JvmSuppressWildcards AnalyticsTracker>,
) : Analytics {

    private val trackersBySupplier: Map<EventSupplier, AnalyticsTracker> =
        trackers.associateBy { it.supplier }

    override fun track(event: AnalyticsEvent, vararg suppliers: EventSupplier) {
        targetsFor(suppliers).forEach { it.track(event) }
    }

    override fun setUserId(id: String?) {
        trackersBySupplier.values.forEach { it.setUserId(id) }
    }

    private fun targetsFor(suppliers: Array<out EventSupplier>): Collection<AnalyticsTracker> = when {
        suppliers.isEmpty() -> listOfNotNull(trackersBySupplier[EventSupplier.Firebase])
        EventSupplier.All in suppliers -> trackersBySupplier.values
        else -> suppliers.toSet().mapNotNull { trackersBySupplier[it] }
    }
}
