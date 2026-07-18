package app.oguzhanozgokce.midmoney.feature.profile.analytics

import app.oguzhanozgokce.midmoney.event.AnalyticsEvent

private const val PROFILE_VIEWED = "profile_viewed"
private const val PROFILE_LOGOUT = "profile_logout"
private const val PROFILE_MENU_CLICKED = "profile_menu_clicked"
private const val PARAM_ITEM = "item"

sealed interface ProfileAnalyticsEvent : AnalyticsEvent {

    data object Viewed : ProfileAnalyticsEvent {
        override val name: String = PROFILE_VIEWED
    }

    data object Logout : ProfileAnalyticsEvent {
        override val name: String = PROFILE_LOGOUT
    }

    data class MenuClicked(val item: String) : ProfileAnalyticsEvent {
        override val name: String = PROFILE_MENU_CLICKED
        override val params: Map<String, String> = mapOf(PARAM_ITEM to item)
    }
}
