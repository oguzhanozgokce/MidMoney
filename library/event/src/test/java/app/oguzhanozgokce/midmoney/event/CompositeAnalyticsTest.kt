package app.oguzhanozgokce.midmoney.event

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CompositeAnalyticsTest {

    private val firebase = FakeTracker(EventSupplier.Firebase)
    private val logcat = FakeTracker(EventSupplier.Logcat)
    private val analytics = CompositeAnalytics(setOf(firebase, logcat))

    @Test
    fun `defaults to firebase when no supplier is given`() {
        analytics.track(TestEvent)

        assertThat(firebase.tracked).containsExactly(TestEvent)
        assertThat(logcat.tracked).isEmpty()
    }

    @Test
    fun `routes only to the requested supplier`() {
        analytics.track(TestEvent, EventSupplier.Logcat)

        assertThat(logcat.tracked).containsExactly(TestEvent)
        assertThat(firebase.tracked).isEmpty()
    }

    @Test
    fun `All routes to every registered tracker`() {
        analytics.track(TestEvent, EventSupplier.All)

        assertThat(firebase.tracked).containsExactly(TestEvent)
        assertThat(logcat.tracked).containsExactly(TestEvent)
    }

    @Test
    fun `setUserId reaches every tracker`() {
        analytics.setUserId("u1")

        assertThat(firebase.userIds).containsExactly("u1")
        assertThat(logcat.userIds).containsExactly("u1")
    }
}

private object TestEvent : AnalyticsEvent {
    override val name: String = "test_event"
}

private class FakeTracker(override val supplier: EventSupplier) : AnalyticsTracker {
    val tracked: MutableList<AnalyticsEvent> = mutableListOf()
    val userIds: MutableList<String?> = mutableListOf()

    override fun track(event: AnalyticsEvent) {
        tracked += event
    }

    override fun setUserId(id: String?) {
        userIds += id
    }
}
