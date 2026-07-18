package app.oguzhanozgokce.midmoney.feature.login.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val LOGIN_CLICKED = "login_clicked"
private const val LOGIN_SUCCEEDED = "login_succeeded"
private const val LOGIN_FAILED = "login_failed"
private const val PARAM_REASON = "reason"

sealed interface LoginAnalyticsEvent : AnalyticsEvent {

    data object LoginClicked : LoginAnalyticsEvent {
        override val name: String = LOGIN_CLICKED
    }

    data object LoginSucceeded : LoginAnalyticsEvent {
        override val name: String = LOGIN_SUCCEEDED
    }

    data class LoginFailed(val reason: String) : LoginAnalyticsEvent {
        override val name: String = LOGIN_FAILED
        override val params: Map<String, String> = mapOf(PARAM_REASON to reason)
    }
}
