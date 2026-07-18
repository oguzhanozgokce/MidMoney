package app.oguzhanozgokce.midmoney.event

import app.oguzhanozgokce.midmoney.logger.MidMoneyLogger
import javax.inject.Inject

class LogcatAnalyticsTracker @Inject constructor() : AnalyticsTracker {

    override val supplier: EventSupplier = EventSupplier.Logcat

    override fun track(event: AnalyticsEvent) {
        MidMoneyLogger.d("event=${event.name} params=${event.params}", tag = TAG)
    }

    override fun setUserId(id: String?) {
        MidMoneyLogger.d("setUserId=$id", tag = TAG)
    }

    private companion object {
        const val TAG = "Analytics"
    }
}
