package app.oguzhanozgokce.midmoney.event

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalyticsClient @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
) : Analytics {

    override fun track(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.name, event.params.toBundle())
    }

    override fun setUserId(id: String?) {
        firebaseAnalytics.setUserId(id)
    }

    private fun Map<String, String>.toBundle(): Bundle = Bundle().apply {
        forEach { (key, value) -> putString(key, value) }
    }
}
