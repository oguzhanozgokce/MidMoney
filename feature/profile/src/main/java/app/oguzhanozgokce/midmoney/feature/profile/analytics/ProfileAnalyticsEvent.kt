package app.oguzhanozgokce.midmoney.feature.profile.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

sealed interface ProfileAnalyticsEvent : AnalyticsEvent {

    data object Viewed : ProfileAnalyticsEvent {
        override val name: String = "profile_viewed"
    }

    data object Logout : ProfileAnalyticsEvent {
        override val name: String = "profile_logout"
    }
}
