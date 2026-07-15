package app.oguzhanozgokce.midmoney.feature.login.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

sealed interface LoginAnalyticsEvent : AnalyticsEvent {

    data object LoginClicked : LoginAnalyticsEvent {
        override val name: String = "login_clicked"
    }

    data object LoginSucceeded : LoginAnalyticsEvent {
        override val name: String = "login_succeeded"
    }

    data class LoginFailed(val reason: String) : LoginAnalyticsEvent {
        override val name: String = "login_failed"
        override val params: Map<String, String> = mapOf("reason" to reason)
    }
}
