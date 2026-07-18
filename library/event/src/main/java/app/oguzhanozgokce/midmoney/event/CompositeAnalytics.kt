package app.oguzhanozgokce.midmoney.event

import javax.inject.Inject

class CompositeAnalytics @Inject constructor(
    trackers: Set<@JvmSuppressWildcards AnalyticsTracker>,
) : Analytics {

    private val trackersBySupplier: Map<EventSupplier, AnalyticsTracker> =
        trackers.associateBy { it.supplier }

    override fun track(event: AnalyticsEvent, vararg suppliers: EventSupplier) {
        val targets = if (suppliers.isEmpty()) DEFAULT_SUPPLIERS else suppliers.asList()
        targets.forEach { supplier -> trackersBySupplier[supplier]?.track(event) }
    }

    override fun setUserId(id: String?) {
        trackersBySupplier.values.forEach { it.setUserId(id) }
    }

    private companion object {
        val DEFAULT_SUPPLIERS = listOf(EventSupplier.Firebase)
    }
}
