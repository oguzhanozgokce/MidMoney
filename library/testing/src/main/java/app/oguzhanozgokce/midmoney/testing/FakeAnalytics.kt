package app.oguzhanozgokce.midmoney.testing

import app.oguzhanozgokce.midmoney.event.Analytics
import app.oguzhanozgokce.midmoney.event.AnalyticsEvent
import app.oguzhanozgokce.midmoney.event.EventSupplier

class FakeAnalytics : Analytics {
    val trackedEvents: MutableList<AnalyticsEvent> = mutableListOf()
    val trackedSuppliers: MutableList<List<EventSupplier>> = mutableListOf()
    var userId: String? = null
        private set

    override fun track(event: AnalyticsEvent, vararg suppliers: EventSupplier) {
        trackedEvents += event
        trackedSuppliers += suppliers.toList()
    }

    override fun setUserId(id: String?) {
        userId = id
    }
}
